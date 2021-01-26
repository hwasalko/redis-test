package com.hwasalko.redis.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Component
public class RedisUtil {
    
    public static Logger logger = LoggerFactory.getLogger(RedisUtil.class);


    private JedisPoolConfig jedisPoolConfig;
    private JedisPool jedisPool;
    private Jedis jedis;
    

    // 생성자
    public RedisUtil(){
        try {
            jedisPoolConfig = new JedisPoolConfig();
            jedisPool = new JedisPool(jedisPoolConfig, "localhost", 6379, 1000); //Jedis풀 생성(JedisPoolConfig, host, port, timeout, password)
            jedis = jedisPool.getResource();//thread, db pool처럼 필요할 때마다 getResource()로 받아서 쓰고 다 쓰면 close로 닫아야 한다.
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }





    // redis 저장
    public void setData(String key, String value){
        logger.info("#### [redis set] key => {} , value => {}", key, value );
        jedis.set(key, value);
    }

    // redis 저장(expire)
    public void setDataExpire(String key, String value, int seconds){
        logger.info("#### [redis set with expire] key => {} , value => {}, expire(seconds) => {}", key, value, seconds );
        jedis.setex(key, seconds, value);
    }

    // redis 조회
    public String getData(String key){
        String result = jedis.get(key);
        logger.info("#### [redis get] key => {} , value => {}", key, result );
        return result;
    }

    // redis 삭제
    public void delData(String key){
        logger.info("#### [redis del] key => {}", key);
        jedis.del(key);
    }


    // redis expire 설정
    public void setExpire(String key, int seconds){
        logger.info("#### [redis expire] key => {} , seconds => {}", key, seconds );
        jedis.expire(key, seconds);
    }






    private void call_redis(){
        
        //데이터 입력
        jedis.set("jeong", "pro");
        //데이터 출력
        logger.info(jedis.get("jeong"));//pro
        //데이터 삭제
        jedis.del("jeong");
        logger.info(jedis.get("jeong"));//null
        
        try {
            jedis.set("key", "value");
            //데이터 만료시간 지정
            jedis.expire("key", 5);//5초 동안만 "key"를 key로 갖는 데이터 유지
            Thread.sleep(4000);//쓰레드를 4초간 재우고
            logger.info(jedis.get("key"));//value
            Thread.sleep(2000);//1초했더니 운좋으면 살아있어서 2초로 지정
            logger.info(jedis.get("key"));//null
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        /* Lists 형태 입출력 */
        jedis.lpush("/home/jdk", "firstTask");
        jedis.lpush("/home/jdk", "secondTask");
        logger.info(jedis.rpop("/home/jdk"));//firstTask
        logger.info(jedis.rpop("/home/jdk"));//secondTask
        
        /* Sets 형태 입출력 */
        jedis.sadd("nicknames", "jeongpro");
        jedis.sadd("nicknames", "jdk");
        jedis.sadd("nicknames", "jeongpro");
        Set<String> nickname = jedis.smembers("nicknames");
        Iterator iter = nickname.iterator();
        while(iter.hasNext()) {
            logger.info(iter.next().toString());
        }
        
        /* Hashes 형태 입출력 */
        jedis.hset("user", "name", "jeongpro");
        jedis.hset("user", "job", "software engineer");
        jedis.hset("user", "hobby", "coding");
        
        logger.info(jedis.hget("user","name"));//jeongpro
        Map<String, String> fields = jedis.hgetAll("user");
        logger.info(fields.get("job"));//software engineer
        
        /* Sorted Sets 형태 입출력 */
        //Map을 미리 만들어서 넣을 수도 있음 zadd확인할 것
        jedis.zadd("scores", 6379.0, "PlayerOne");
        jedis.zadd("scores", 8000.0, "PlayerTwo");
        jedis.zadd("scores", 1200.5, "PlayerThree");
        
        logger.info( jedis.zrangeByScore("scores", 0, 10000).toString() );
        //[PlayerThree, PlayerOne, PlayerTwo]
        //Sorted Sets는 잘모르겠으니 더 공부할 것.
        
        
        if(jedis != null) {
            jedis.close();
        }
        jedisPool.close();


    }


}
