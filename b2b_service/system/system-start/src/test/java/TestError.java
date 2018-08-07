import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/19 16:02
 */
public class TestError {

    private static Set<String> strings = new HashSet<>();

    public static void main(String[] args)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String url = "D:\\workspace\\B2B\\system-service";
        File dir = new File(url);// 浏览F盘a文件夹下的所有内容
        listFile(dir, "");
        Map<String, String> new_ = new HashMap<String, String>();
        strings.forEach(s -> {
            new_.put(s, null);
        });
        Properties properties = new Properties();
        properties.load(new FileReader(
            new File("D:\\workspace\\B2B\\system-service\\system-start\\src\\main\\resources\\messages.properties")));
        Map<String, String> old = new HashMap<String, String>((Map)properties);
        System.out.println(new_);
        old.forEach((s, s2) -> {
            new_.putIfAbsent(s, s2);
        });
        List<String> strings1 = new ArrayList<>();
        new_.forEach((s, s2) -> {
            strings1.add(s + "=" + s2);
        });
        List<String> collect = strings1.stream().sorted().collect(Collectors.toList());
        collect.forEach(s -> {
            System.out.println(s);
        });
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
                    Class<?> aClass = Class.forName(name);
                    Object obj = aClass.newInstance();
                    Field[] fields = aClass.getFields();
                    Arrays.stream(fields).forEach(field -> {
                        field.setAccessible(true);
                        synchronized (TestError.class) {
                            strings.add(field.getName());
                        }
                        // System.out.println(field.getName() + "=");
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
                            System.out.println(aClass1);
                            if (aClass1.getName().endsWith("Cmd") || aClass1.getName().endsWith("Qry")
                                || aClass1.getName().endsWith("Id") || aClass1.getName().endsWith("Ids")) {
                                Field[] fields = aClass1.getDeclaredFields();
                                Arrays.stream(fields).forEach(field -> {
                                    NotBlank notBlank = field.getAnnotation(NotBlank.class);
                                    NotNull notBull = field.getAnnotation(NotNull.class);
                                    if (notBlank != null) {
                                        String message = notBlank.message();
                                        synchronized (TestError.class) {
                                            strings.add(message);
                                        }
                                    }
                                    if (notBull != null) {
                                        String message = notBull.message();
                                        synchronized (TestError.class) {
                                            strings.add(message);
                                        }
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
