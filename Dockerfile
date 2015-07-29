#FROM ubuntu:trusty
FROM daocloud.io/ubuntu:14.04
MAINTAINER shaoping.liu <spieled916@gmail.com>
RUN sed -i 's/archive.ubuntu/mirrors.163/g' /etc/apt/sources.list
RUN sed -i 's/us\.archive\.ubuntu\.com/mirrors.163.com/g' /etc/apt/sources.list
RUN apt-get update  && apt-get install wget -y

# Set the locale
RUN apt-get install language-pack-zh-hans -y
ENV LANG en_US.UTF-8
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
RUN wget -q https://raw.githubusercontent.com/spieled/soft/master/server-jre-7u80-linux-x64.gz
RUN tar zxf server-jre-7u80-linux-x64.gz
RUN rm -f server-jre-7u80-linux-x64.gz
RUN mkdir -p /usr/java/
RUN mv jdk1.7* /usr/java/jdk7

RUN echo "export JAVA_HOME=/usr/java/jdk7/jre" >> /etc/profile
RUN echo "export PATH=\$JAVA_HOME/bin/:\$PATH" >> /etc/profile

# Time Zone
RUN echo "Asia/Shanghai" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata

RUN wget -q https://raw.githubusercontent.com/spieled/soft/master/tomcat7.63.tgz
RUN wget -q https://raw.githubusercontent.com/spieled/soft/master/tomcat-apr_new.tgz
RUN tar xzf tomcat7.63.tgz
RUN tar xzf tomcat-apr_new.tgz
RUN wget -q https://raw.githubusercontent.com/spieled/soft/master/start.sh 
RUN chmod +x start.sh
RUN rm -rf tomcat7.63.tgz
RUN rm -rf tomcat-apr_new.tgz

ADD ./catalina.sh /tomcat7/bin/catalina.sh
RUN chmod +x /tomcat7/bin/catalina.sh

ADD ./scorp-webapp/target/scorp-webapp.war /tomcat7/webapps/ROOT.war

EXPOSE 8080
CMD /start.sh && sleep 5 && tail -f /tomcat7/logs/catalina.out 
