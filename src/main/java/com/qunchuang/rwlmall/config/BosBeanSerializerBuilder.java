package com.qunchuang.rwlmall.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 *  本类用来定制服务器返回对象转成JSON字符串的序列化规则，即如何如何把java对象转成JSON对象，以便返回给前端。
 *  当前主要是针对hibernate采用proxy实现延迟加载如何转化的问题
 * Created by liutim on 2018/1/1.
 */
class BosBeanSerializerBuilder extends BeanSerializerBuilder {

    public final static void configMappingJackson2HttpMessageConverter(MappingJackson2HttpMessageConverter mj2HttpMessageConverter) {
        final ObjectMapper objectMapper= mj2HttpMessageConverter.getObjectMapper();
        objectMapper.setSerializerFactory(new BeanSerializerFactory(BeanSerializerFactory.instance.getFactoryConfig()){
            @Override
            protected BeanSerializerBuilder constructBeanSerializerBuilder(BeanDescription beanDesc) {
                return new BosBeanSerializerBuilder(beanDesc);
            }
            @Override
            public JsonSerializer<Object> createSerializer(SerializerProvider prov,
                                                           JavaType origType)  throws JsonMappingException {
                Class c=origType.getRawClass();

                if(HibernateProxy.class.isAssignableFrom(c)){
                    origType=objectMapper.getSerializationConfig().constructType(LaundryProduct.class);
                    // TODO: 2018/3/9   修改  不应只是写死class的类型
                }
                return super.createSerializer(prov,origType);
            }


        });
    }

    public BosBeanSerializerBuilder(BeanDescription beanDesc) {
        super(beanDesc);
    }

    @Override
    public JsonSerializer<?> build() {
        JsonSerializer<?> jsonSerializer = super.build();
        if (jsonSerializer instanceof BeanSerializer) {
            return new BosBeanSerializer((BeanSerializer) jsonSerializer);
        } else {
            return jsonSerializer;
        }
    }


    /***

     * 本类的职责是处理Object转JSON是，如果有Hibernate Proxy对象如何正确处理。
     * 因为JPA延迟加载LazyLoading情况下，这个延迟加载对象只是一个hibernate proxy。
     * 1.当是proxy且未被初始化时，只有id属性被设置了，应该只序列化id属性。
     * 2.如果是proxy且已经被初始化，则需要按照它所代理的类的属性（为了做到这一点需要上面的自定义BeanSerializerFactory)，去序列化它所代理的对象。
     * 3.如果不是proxy而是正常的IEntity实体对象,则正常序列化
     *
     */
    class BosBeanSerializer extends BeanSerializer {

        private final StringSerializer STRINGSERIALIZER = new StringSerializer();

        /**
         * Alternate copy constructor that can be used to construct
         * standard {@link BeanSerializer} passing an instance of
         * "compatible enough" source serializer.
         */
        public BosBeanSerializer(BeanSerializerBase src) {
            super(src);
        }


        @Override
        protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (bean != null && HibernateProxy.class.isAssignableFrom(bean.getClass())) {
                HibernateProxy proxy = (HibernateProxy) bean;
                if (proxy.getHibernateLazyInitializer().isUninitialized()) {
                    Serializable sid = ((HibernateProxy) bean).getHibernateLazyInitializer().getIdentifier();
                    gen.writeFieldName("id");
                    STRINGSERIALIZER.serialize(sid, gen, provider);
                } else {
                    //此处如果缓存中存在已加载对象，似乎能拿到！ TODO 要对JPA以及hibernate的二级缓存有所了解
                    super.serializeFields(proxy.getHibernateLazyInitializer().getImplementation(), gen, provider);
                }
            } else {
                super.serializeFields(bean, gen, provider);
            }
        }

    }

}



