import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/19 16:02
 */
public class TestError {
    public static void main(String[] args)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String url = "D:\\workspace\\B2B\\financial-service";
        File dir = new File(url);// 浏览F盘a文件夹下的所有内容
        listFile(dir, "");
    }

    public static void listFile(File dir, String spance)
        throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        File[] files = dir.listFiles(); // 列出所有的子文件
        for (File file : files) {
            if (file.isFile())// 如果是文件，则输出文件名字
            {
                if (file.getName().endsWith("ErrorCode.java")) {
                    // System.out.println(spance + file.getName());
                    // System.out.println(file.getAbsolutePath());
                    String name = getName(file.getAbsolutePath());
                    System.out.println("#" + name);
                    Class<?> aClass = Class.forName(name);
                    Object obj = aClass.newInstance();
                    Field[] fields = aClass.getFields();
                    Arrays.stream(fields).forEach(field -> {
                        field.setAccessible(true);
                        System.out.println(field.getName() + "=");
                    });
                }
                if (file.getName().endsWith("Controller.java")) {
                    String name = getName(file.getAbsolutePath());
                    // System.out.println("#" + name);
                    Class<?> aClass = Class.forName(name);
                    Method[] methods = aClass.getMethods();
                    Arrays.stream(methods).forEach(method -> {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Arrays.stream(parameterTypes).forEach(aClass1 -> {
                            if (aClass1.getName().endsWith("Cmd") || aClass1.getName().endsWith("Qry")
                                || aClass1.getName().endsWith("Id") || aClass1.getName().endsWith("Ids")) {
                                Field[] fields = aClass1.getDeclaredFields();
                                Arrays.stream(fields).forEach(field -> {
                                    NotBlank notBlank = field.getAnnotation(NotBlank.class);
                                    NotNull notBull = field.getAnnotation(NotNull.class);
                                    if (notBlank != null) {
                                        System.out.println("#" + aClass1);
                                        String message = notBlank.message();
                                        System.out.println(message + "=");
                                    }
                                    if (notBull != null) {
                                        System.out.println("#" + aClass1);
                                        String message = notBull.message();
                                        System.out.println(message + "=");
                                    }
                                });
                            }
                        });
                    });
                }
            } else if (file.isDirectory())// 如果是文件夹，则输出文件夹的名字，并递归遍历该文件夹
            {
                // System.out.println(spance + file.getName());
                listFile(file, "|--" + spance);// 递归遍历
            }
        }
    }

    public static String getName(String url) {
        String com = url.substring(url.indexOf("com"));
        String substring = com.substring(0, com.indexOf("."));
        String replace = substring.replace("\\", ".");
        return replace;
    }
}
