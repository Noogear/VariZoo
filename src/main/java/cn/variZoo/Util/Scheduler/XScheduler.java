package cn.variZoo.Util.Scheduler;

import cn.variZoo.Main;

public class XScheduler {

    public static XScheduler instance;
    private final IScheduler scheduler;

    public XScheduler(Main main, Boolean isFolia) {

        instance = this;
        if (isFolia) {
            this.scheduler = new FoliaScheduler(main);
        } else {
            this.scheduler = new CommonScheduler(main);
        }

    }

    public static IScheduler get() {
        return instance.scheduler;
    }

}
