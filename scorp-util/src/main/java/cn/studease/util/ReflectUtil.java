package cn.studease.util;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.LoggerFactory;

/**
 * Author: liushaoping
 * Date: 2015/7/19.
 */
public class ReflectUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ReflectUtil.class);

    public static <S, T> T dump(S source, T target) {
        if (source == null) {
            return target;
        }
        List<Method> methods = new ArrayList();
        Class<?> c = source.getClass();
        while (!c.getName().equals("java.lang.Object")) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
            c = c.getSuperclass();
        }
        for (Method method : methods) {
            try {
                String methodName = method.getName();
                String setter;
                if (methodName.startsWith("get")) {
                    setter = "set" + methodName.substring(3);
                } else {
                    if (methodName.startsWith("is"))
                        setter = "set" + methodName.substring(2);
                    else
                        continue;
                }
                target.getClass().getMethod(setter, new Class[]{method.getReturnType()}).invoke(target, new Object[]{method.invoke(source, new Object[0])});
            } catch (Exception ignored) {
                log.trace("dump出现异常，通常是由于源或目标缺少指定字段造成的。", ignored);
            }
        }
        return target;
    }


    public static <S, T> T dump(S source, T target, String... fields) {
        if (source == null) {
            return target;
        }


        Class<?> sourceType = source.getClass();
        Class<?> targetType = target.getClass();


        for (String field : fields) {
            try {
                getSetter(targetType, field).invoke(target, new Object[]{getGetter(sourceType, field).invoke(source, new Object[0])});
            } catch (Exception e) {
                log.trace("dump出现异常，通常是由于源或目标缺少指定字段造成的。", e);
            }
        }
        return target;
    }


    public static <T> T clone(T object) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteStream);
            out.writeObject(object);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(new java.io.ByteArrayInputStream(byteStream.toByteArray()));
            T cloned = (T) in.readObject();
            in.close();

            return cloned;
        } catch (Exception e) {
            log.trace("克隆对象失败", e);
        }
        return null;
    }


    public static <T> Method getGetter(Class<T> clazz, String field) {
        try {
            String prefix = getField(clazz, field).getType().getSimpleName().toLowerCase().equals("boolean") ? "is" : "get";
            String methodName = prefix + StringUtil.upperCaseFirstChar(field);
            return clazz.getMethod(methodName, new Class[0]);
        } catch (Exception e) {
            log.trace("获取Getter方法失败", e);
        }
        return null;
    }


    public static <T> Method getSetter(Class<T> clazz, String field) {
        try {
            Class<?> type = getField(clazz, field).getType();
            String methodName = "set" + StringUtil.upperCaseFirstChar(field);
            return clazz.getMethod(methodName, new Class[]{type});
        } catch (Exception e) {
            log.trace("获取Setter方法失败", e);
        }
        return null;
    }


    public static <T> Method getMethod(Class<T> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (Exception e) {
            log.trace("获取Method失败", e);
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String field) {
        Field f = null;
        while ((f == null) && (!clazz.getName().equals("java.lang.Object"))) {
            try {
                f = clazz.getDeclaredField(field);
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }
        return f;
    }


    public static List<Field> getFields(Class<?> clazz, Class<?> type, boolean ignoreStatic, boolean ignoreTransient, String[] ignoredFields) {
        List<Field> result = new ArrayList();
        Set<String> fieldNames = new HashSet();
        Set<String> ignoredFieldList = new HashSet();
        if ((ignoredFields != null) && (ignoredFields.length > 0)) {
            ignoredFieldList.addAll(Arrays.asList(ignoredFields));
        }
        while (!clazz.getName().equals("java.lang.Object")) {
            for (Field f : clazz.getDeclaredFields())
                if ((type == null) || (f.getType().equals(type))) {

                    if ((!ignoreStatic) || (!java.lang.reflect.Modifier.isStatic(f.getModifiers()))) {

                        if ((!ignoreTransient) || (f.getAnnotation(java.beans.Transient.class) == null)) {

                            if ((!ignoredFieldList.contains(f.getName())) && (!fieldNames.contains(f.getName()))) {

                                fieldNames.add(f.getName());
                                result.add(f);
                            }
                        }
                    }
                }
            clazz = clazz.getSuperclass();
        }
        return result;
    }


    public static List<Field> getFields(Class<?> clazz, Class<?> type) {
        return getFields(clazz, type, true, true, null);
    }


    public static List<Field> getFields(Class<?> clazz) {
        return getFields(clazz, null);
    }


    public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Field> result = new ArrayList();
        for (Field field : getFields(clazz)) {
            if (field.getAnnotation(annotation) != null) {
                result.add(field);
            }
        }
        return result;
    }

    public static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList();
        String packageDirName = packageName.replaceAll("\\.", "/");
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                if ("jar".equals(url.getProtocol())) {
                    JarFile jarFile;
                    try {
                        JarURLConnection conn = (JarURLConnection) url.openConnection();
                        jarFile = conn.getJarFile();
                    } catch (IOException e) {
                        continue;
                    }


                    for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                        JarEntry entry = (JarEntry) entries.nextElement();
                        String entryName = entry.getName();
                        if ((entryName.startsWith(packageDirName)) && (entryName.endsWith(".class")) && (!entryName.contains("$"))) {
                            String className = entryName.replace("/", ".").substring(0, entryName.length() - ".class".length());
                            try {
                                classes.add(Class.forName(className));
                            } catch (ClassNotFoundException ignored) {
                                log.trace("无法正确获取Class", ignored);
                            }
                        }
                    }
                } else if ("file".equals(url.getProtocol())) {
                    String filePath;
                    try {
                        filePath = java.net.URLDecoder.decode(url.getFile(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        log.trace("无法正确获取文件，不支持的编码", e);
                        continue;
                    }

                    getClassesFromDirectory(packageName, new File(filePath), classes);
                }
            }
        } catch (IOException ignored) {
            log.trace("无法正确获取Class，出现IO异常", ignored);
        }
        return classes;
    }


    private static void getClassesFromDirectory(String packageName, File directory, List<Class<?>> classes) {
        if ((!directory.exists()) || (!directory.isDirectory())) {
            return;
        }
        File[] files = directory.listFiles(new java.io.FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isDirectory()) || ((pathname.getName().endsWith(".class")) && (!pathname.getName().contains("$")));
            }
        });
        for (File file : files) {
            if (file.isDirectory()) {
                getClassesFromDirectory(packageName + "." + file.getName(), file, classes);
            } else {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException ignored) {
                    log.trace("无法正确获取类", ignored);
                }
            }
        }
    }

}
