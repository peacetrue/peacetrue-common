package com.github.peacetrue.util;

import com.github.peacetrue.net.URLCodecUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author peace
 **/
class RefTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(() -> new Ref<>());
        Assertions.assertDoesNotThrow(() -> new Ref<>(0));
    }


    @Test
    void lambda() {
        Ref<Integer> index = new Ref<>(0);
        IntStream.range(0, 10).boxed().flatMap(i -> Stream.of(
                new Thread(() -> System.out.println("write"), "thread-write-" + index.value++),
                new Thread(() -> System.out.println("read"), "thread-read-" + index.value++)
        )).forEach(Thread::start);
    }

    @Test
    void anonymous() {

//    int index = 0;
//    new Object() {
//        {
//            System.out.println(index);
//        }
//    };
//    index = 1;

        Ref<Integer> index = new Ref<>(0);
        new Object() {
            {
                System.out.println(index.value);
            }
        };
        index.value = 1;
    }

    void encode() {
        System.out.println(URLCodecUtils.encode(
                "Ref<Integer> index = new Ref<>(0);\n" +
                        "        new Object() {\n" +
                        "            {\n" +
                        "                System.out.println(index.value);\n" +
                        "            }\n" +
                        "        };\n" +
                        "        index.value = 1;"
        ));
    }
}
