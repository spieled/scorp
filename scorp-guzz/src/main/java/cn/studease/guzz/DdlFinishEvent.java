package cn.studease.guzz;

import cn.studease.guzz.annotation.J;
import org.springframework.context.ApplicationEvent;

@J("DDL完成事件")
public class DdlFinishEvent extends ApplicationEvent {
    private static final long serialVersionUID = 6694915799202815414L;

    public DdlFinishEvent() {
        super("");
    }
}


