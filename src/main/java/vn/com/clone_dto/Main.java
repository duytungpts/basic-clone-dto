package vn.com.clone_dto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final List<String> DTO_CLASS_NAMES = Arrays.asList("CustomerDto", "OrderDto", "ProductDto", "UserDto");

    private static final String CLASS_OUTPUT_DIR = "src/main/java/vn/com/clone_dto/req_clone_dto"; // cập nhật đúng build path

    private static void collectImports(Type t, Set<String> imports) {
        if (t instanceof Class<?> c && needImport(c)) imports.add(c.getName());
        if (t instanceof ParameterizedType pt) {
            collectImports(pt.getRawType(), imports);
            for (Type arg : pt.getActualTypeArguments()) collectImports(arg, imports);
        }
    }

    private static boolean needImport(Class<?> c) {
        return !c.isPrimitive() && !c.getPackageName().startsWith("java.lang");
    }

    private static String getTypeName(Type t) {
        if (t instanceof Class<?> c) return c.getSimpleName();
        if (t instanceof ParameterizedType pt) {
            String raw = getTypeName(pt.getRawType());
            String args = Arrays.stream(pt.getActualTypeArguments())
                    .map(Main::getTypeName)
                    .reduce((a, b) -> a + ", " + b).orElse("");
            return raw + "<" + args + ">";
        }
        return "Object";
    }


    private static void generateJavaFile(Class<?> dtoClass, String newClassName) throws IOException {
        StringBuilder sb = new StringBuilder();
        Set<String> imports = new HashSet<>();

        for (Field f : dtoClass.getDeclaredFields()) {
            if (!Modifier.isPrivate(f.getModifiers())) continue;
            collectImports(f.getGenericType(), imports);
        }

        sb.append("package vn.com.clone_dto.req_clone_dto;\n\n"); // ✅ Thêm package
        sb.append("import lombok.Getter;\n");
        for (String imp : imports) sb.append("import ").append(imp).append(";\n");
        sb.append("\n@Getter\npublic class ").append(newClassName).append(" {\n\n");

        for (Field f : dtoClass.getDeclaredFields()) {
            if (!Modifier.isPrivate(f.getModifiers())) continue;
            sb.append("    private ").append(getTypeName(f.getGenericType()))
                    .append(" ").append(f.getName()).append(";\n");
        }

        sb.append("}\n");

        // ✅ Tạo thư mục nếu chưa có
        Path outputDir = Paths.get("src/main/java/vn/com/clone_dto/req_clone_dto");
        Files.createDirectories(outputDir);

        // ✅ Tạo file trong đúng thư mục
        Path outputFile = outputDir.resolve(newClassName + ".java");
        try (var w = new FileWriter(outputFile.toFile())) {
            w.write(sb.toString());
        }

        System.out.println("✅ Đã tạo file: " + outputFile);
    }

    public static void main(String[] args) throws IOException {
        // Load thư mục biên dịch chứa các class .class
        File classDir = new File(CLASS_OUTPUT_DIR);
        URLClassLoader classLoader = new URLClassLoader(new URL[]{classDir.toURI().toURL()});
        Path sourceRoot = Paths.get("src");

        for (String dtoName : DTO_CLASS_NAMES) {


            try {
                // Tìm file .java tương ứng
                Optional<Path> javaFileOpt = Files.walk(sourceRoot).filter(p -> p.getFileName().toString().equals(dtoName + ".java")).findFirst();

                if (javaFileOpt.isEmpty()) {
                    System.out.println("Không tìm thấy file " + dtoName + ".java");
                    return;
                }

                Path javaFile = javaFileOpt.get();
                String className = Files.readAllLines(javaFile).stream()
                        .filter(line -> line.startsWith("package "))
                        .map(line -> line.replace("package", "").replace(";", "").trim())
                        .findFirst()
                        .orElse("") + "." + dtoName;

                // Load class từ .class đã biên dịch
                Class<?> clazz = classLoader.loadClass(className);

                generateJavaFile(clazz, dtoName.concat("Req"));
            } catch (ClassNotFoundException e) {
                System.err.println("Không tìm thấy class: " + dtoName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        classLoader.close();
    }
}