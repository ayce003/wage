package com.ycjd.payslip.pojo;

public interface Constant {
    int HAVEN_LOGIN=909;//未登录
    String JWT_SECRET="7786df7fc3a34e26a61c034d5ec8245d";
    String TOKEN="token";//登录的请求令牌名称
    String INITIAL_PASSWORD="66666666";//登录初始密码
    String DEFAULT_ROLE_STATUS="1";

    interface Redis {
        String WORKER_CACHE ="ycjd-wage-workerLogin";
        String WORKER_ID_HEADER="ycjd-wage-worker_id";
        int WORKER_CACHE_TIMEOUT=3600;
        String DEPT_CACHE="deptCache";
        String CODE_CACHE="ycjd-wage-code";
        String CODE_IMG="ycjd-wage-codeimg";
        int CODE_IMG_CACHE_TIMEOUT=60;
    }

    /**
     *  性别常量
     */
    interface SEX_TYPE{
        String MAN = "男";//男
        String WOMAN="女";//女
    }


    /**
     *  角色常量
     */
    interface Rule_TYPE{
        String ADMIN = "1";//管理员
        String COMMON_EMPLOYEE="2";//普通员工
    }


    interface Rule_NAME{
        String ADMIN_NAME = "管理员";//管理员
        String COMMON_EMPLOYEE_NAME="普通员工";//普通员工
    }

    /**
     * mcs消息消费服务中用到的渠道
     */
    interface MCS_CHANEL{
        String AUTH_IDENTIFY="authIdentify";//身份验证通道
    }


    interface Overtime{
        String UNAUDITED="0"; //未审核
        String AUDITED="1";  //已审核
        String DEFAULTSTATUS="0";//人事初始审核状态
    }

    interface Kaptcha{
         String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

         String KAPTCHA_SESSION_DATE = "KAPTCHA_SESSION_DATE";

         String KAPTCHA_SESSION_CONFIG_KEY = "kaptcha.session.key";

         String KAPTCHA_SESSION_CONFIG_DATE = "kaptcha.session.date";
        // 图片边框，合法值：yes , no
         String KAPTCHA_BORDER = "kaptcha.border";
        // 边框颜色，合法值： rgb (and optional alpha) 或者 white,black,blue.
         String KAPTCHA_BORDER_COLOR = "kaptcha.border.color";
        // 边框厚度，合法值：>0
         String KAPTCHA_BORDER_THICKNESS = "kaptcha.border.thickness";
        // 干扰颜色，合法值： r,g,b 或者 white,black,blue.
         String KAPTCHA_NOISE_COLOR = "kaptcha.noise.color";
        // 干扰实现类
         String KAPTCHA_NOISE_IMPL = "kaptcha.noise.impl";
        // 图片样式：
        // 水纹com.google.code.kaptcha.impl.WaterRipple
        // 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy
        // 阴影com.google.code.kaptcha.impl.ShadowGimpy
         String KAPTCHA_OBSCURIFICATOR_IMPL = "kaptcha.obscurificator.impl";
        // 背景实现类
         String KAPTCHA_PRODUCER_IMPL = "kaptcha.producer.impl";
        // 文本实现类
         String KAPTCHA_TEXTPRODUCER_IMPL = "kaptcha.textproducer.impl";
        // 文本集合，验证码值从此集合中获取
         String KAPTCHA_TEXTPRODUCER_CHAR_STRING = "kaptcha.textproducer.char.string";
        // 验证码长度
         String KAPTCHA_TEXTPRODUCER_CHAR_LENGTH = "kaptcha.textproducer.char.length";
        // 字体
         String KAPTCHA_TEXTPRODUCER_FONT_NAMES = "kaptcha.textproducer.font.names";
        // 字体颜色，合法值： r,g,b  或者 white,black,blue.
         String KAPTCHA_TEXTPRODUCER_FONT_COLOR = "kaptcha.textproducer.font.color";
        // 字体大小
         String KAPTCHA_TEXTPRODUCER_FONT_SIZE = "kaptcha.textproducer.font.size";
        // 文字间隔
         String KAPTCHA_TEXTPRODUCER_CHAR_SPACE = "kaptcha.textproducer.char.space";
        // 文字渲染器
         String KAPTCHA_WORDRENDERER_IMPL = "kaptcha.word.impl";
        // 背景实现类
         String KAPTCHA_BACKGROUND_IMPL = "kaptcha.background.impl";
        // 背景颜色渐变，开始颜色
         String KAPTCHA_BACKGROUND_CLR_FROM = "kaptcha.background.clear.from";
        // 背景颜色渐变，结束颜色
         String KAPTCHA_BACKGROUND_CLR_TO = "kaptcha.background.clear.to";
        // 图片宽
         String KAPTCHA_IMAGE_WIDTH = "kaptcha.image.width";
        // 图片高
         String KAPTCHA_IMAGE_HEIGHT = "kaptcha.image.height";
    }
}
