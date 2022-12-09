package item9;

import java.io.*;

public class MainOfItem9 {
    static final int BUFFER_SIZE = 10;

    // 9.1) try-finally : 더 이상 자원을 회수하는 최선의 방법이 아니다. 자원이 많아지면 ? 9.2
    /*
    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }
    */

    // 9.2) 자원이 둘 이상이면 try-finally 방식은 너무 지저분하다 !!
    /*
    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0) out.write(buf, 0, n);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
    */

    // 9.3) try-with-resources : 자원을 회수하는 최선책!
    /*
    static String firstLineOfFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
    }
    */

    // 9.4) 복수의 자원을 처리하는 try-with-resources : 아주 깔끔해..
    static void copy(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) out.write(buf, n, 0);
        }
    }

    // 9.5) catch절과 함께 쓰는 try-with-resources
    static String firstLineOfFile(String path, String defaultVal) {
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            return br.readLine();
        } catch (IOException e){
            e.printStackTrace();
            return defaultVal;
        }
    }
}
