package javaagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    /*
    해당 예제는 java agent 를 테스트하기 위한 class 로 해당 예제 자체는 별 의미가 없으며 실행 시
    VM Option에 다음 항목을 추가해 주어야 한다
    -javaagent:"D:\tutorials-master\asm\target\asm-1.0.jar"
     */
    public static void main(String[] args) {
        Integer a = 3;
        logger.info("Integer value : {}", a);
    }
}
