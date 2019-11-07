package com.ryuntech.common.utils;

import com.ryuntech.common.model.BaseDto;
import com.ryuntech.common.model.BaseForm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EDZ
 */
public class CopyUtil {

    public static void copyObject(BaseForm src, BaseDto dest) {
        Map<String, Object> srcMap = new HashMap<String, Object>();

        Field[] srcFields = src.getClass().getDeclaredFields();

        for (Field fd : srcFields) {

            try {
                /**
                 * 获取属性值
                 */
                srcMap.put(fd.getName(), fd.get(src));

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        Field[] destFields = dest.getClass().getDeclaredFields();

        for (Field fd :
                destFields) {

            if (srcMap.get(fd.getName()) == null) {

                continue;

            }
            try {
                /**
                 * 给属性复值
                 */
                fd.set(dest, srcMap.get(fd.getName()));

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }


    public static void copyObject2(BaseForm src, BaseDto dest) {
        Map<String, Object> srcMap = new HashMap<String, Object>();

        Field[] srcFields = src.getAClass().getDeclaredFields();

        for (Field fd : srcFields) {

            try {
                /**
                 * 获取属性值
                 */
                fd.setAccessible(true);
                srcMap.put(fd.getName(), fd.get(src.getT()));

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        Field[] destFields = dest.getAClass().getDeclaredFields();

        for (Field fd :
                destFields) {

            if (srcMap.get(fd.getName()) == null) {

                continue;

            }
            try {
                /**
                 * 给属性复值
                 */
                fd.setAccessible(true);
                fd.set(dest.getT(), srcMap.get(fd.getName()));

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

}
