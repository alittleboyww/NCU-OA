package com.ncu.oa.admin.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/9 0009
 * Time:19:55
 */
public class MD5SaltUtil {
    public static Object getMd5SaltUtil(String saltString, String credentials){
        String hashAlgorithmName = "MD5";
        ByteSource salt = ByteSource.Util.bytes(saltString);
        System.out.println(salt);
        int hashIterations = 1024;//加密1024次
        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(((SimpleHash) result).toHex());
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getMd5SaltUtil("123","123456"));;
    }
}
