package com.github.peacetrue.bean;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * 深度克隆微机准测试。
 *
 * @author peace
 * @see <a href="https://stackoverflow.com/questions/64036/how-do-you-make-a-deep-copy-of-an-object">How do you make a deep copy of an object?</a>
 **/
@Slf4j
@State(Scope.Benchmark)
@Warmup
@Measurement
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CloneBeanVisitorBenchmark {

    @Benchmark
    public void baseline(Blackhole blackhole) {
    }

    private final JsonMapper jsonMapper = new JsonMapper();

    @Benchmark
    public void cloneByJackson(Blackhole blackhole) {
        try {
            byte[] bytes = jsonMapper.writeValueAsBytes(BeanCommon.USER);
            blackhole.consume(jsonMapper.readValue(bytes, BeanCommon.User.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void cloneBySerializable(Blackhole blackhole){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(BeanCommon.USER);
            oos.flush();
            oos.close();
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray())
            );
            blackhole.consume(objectInputStream.readObject());
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void cloneByBeanVisitor(Blackhole blackhole) {
        blackhole.consume(CloneBeanVisitor.DEFAULT.visitRoot(BeanCommon.USER));
    }
    //Benchmark                                             Mode  Cnt      Score   Error  Units
    //CloneBeanVisitorBenchmark.baseline                    avgt    2      0.359          ns/op
    //CloneBeanVisitorBenchmark.baseline:·stack             avgt             NaN            ---
    //CloneBeanVisitorBenchmark.cloneByBeanVisitor          avgt    2   7815.711          ns/op
    //CloneBeanVisitorBenchmark.cloneByBeanVisitor:·stack   avgt             NaN            ---
    //CloneBeanVisitorBenchmark.cloneByJackson              avgt    2   2311.856          ns/op
    //CloneBeanVisitorBenchmark.cloneByJackson:·stack       avgt             NaN            ---
    //CloneBeanVisitorBenchmark.cloneBySerializable         avgt    2  31347.436          ns/op
    //CloneBeanVisitorBenchmark.cloneBySerializable:·stack  avgt             NaN            ---

    //....[Thread state: RUNNABLE]........................................................................
    //  5.7%  17.1% java.util.stream.StreamOpFlag.fromCharacteristics
    //  4.7%  14.2% java.util.stream.ReferencePipeline.forEachWithCancel
    //  4.7%  13.9% com.github.peacetrue.bean.DescriptorBeanProperty.getValue
    //  3.9%  11.6% com.github.peacetrue.bean.DescriptorBeanProperty.setValue
    //  2.6%   7.8% sun.reflect.DelegatingMethodAccessorImpl.invoke
    //  1.3%   3.9% java.util.HashMap.hash
    //  1.3%   3.9% java.util.AbstractList.iterator
    //  1.1%   3.3% java.util.stream.MatchOps$1MatchSink.<init>
    //  1.1%   3.2% java.util.AbstractList.hashCode
    //  0.8%   2.4% java.util.HashMap.put
    //  6.2%  18.6% <other>

    //....[Thread state: RUNNABLE]........................................................................
    //  8.4%  24.9% sun.reflect.DelegatingMethodAccessorImpl.invoke
    //  3.6%  10.8% com.fasterxml.jackson.databind.deser.BeanDeserializer.vanillaDeserialize
    //  3.6%  10.7% com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField
    //  2.7%   8.2% com.fasterxml.jackson.core.json.UTF8StreamJsonParser.getText
    //  1.8%   5.5% com.fasterxml.jackson.databind.deser.impl.MethodProperty.deserializeAndSet
    //  1.3%   3.9% com.fasterxml.jackson.core.json.UTF8StreamJsonParser.nextTextValue
    //  1.1%   3.3% com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields
    //  1.1%   3.3% com.fasterxml.jackson.core.json.UTF8StreamJsonParser._parsePosNumber
    //  1.1%   3.2% sun.reflect.DelegatingConstructorAccessorImpl.newInstance
    //  0.9%   2.6% com.fasterxml.jackson.core.json.JsonWriteContext.createChildObjectContext
    //  7.9%  23.6% <other>

}
