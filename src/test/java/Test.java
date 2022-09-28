import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class Test {

    public static void main(String[] args) {
        System.out.println(DateUtil.beginOfDay(new Date()).toString());
    }
}
