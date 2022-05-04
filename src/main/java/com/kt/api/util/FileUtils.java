package com.kt.api.util;

import com.kt.api.model.file.FileData;
import org.apache.commons.lang.SystemUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * File Utility.
 */
public class FileUtils {

    public static long KB = 1024L;
    public static long MB = KB * KB;
    public static long GB = MB * KB;
    public static long TB = GB * KB;
    public static long PB = TB * KB;
    public static long ZB = PB * KB;

    /**
     * 지정한 파일의 최근 변경된 시간을 반환한다.
     *
     * @param filename 파일명
     * @return 변경된 시간. 만약에 파일이 존재하지 않거나 입출력 에러가 발생하면 0L을 반환한다.
     */
    public static long lastModified(final String filename) {
        return new File(filename).lastModified();
    }

    /**
     * 지정한 경로에서 파일명만 추출한다. 예) "mypath/myfile.txt" -> "myfile.txt".
     *
     * @param path 파일 경로(<tt>null</tt>이 될 수도 있음)
     * @return 추출된 파일명 또는 파일명이 없는 경우 <tt>null</tt>
     */
    public static String getFilename(String path) {
        return org.springframework.util.StringUtils.getFilename(path);
    }

    /**
     * 지정한 경로에서 파일의 확장자를 추출한다. 예) "mypath/myfile.txt" -> "txt".
     *
     * @param path 파일 경로(<tt>null</tt>이 될 수도 있음)
     * @return the extracted filename extension, or <tt>null</tt> if none
     */
    public static String getFilenameExtension(String path) {
        return org.springframework.util.StringUtils.getFilenameExtension(path);
    }

    /**
     * 지정한 경로에서 파일의 확장자를 제외한 경로를 반환한다. 예) "mypath/myfile.txt" -> "mypath/myfile".
     *
     * @param path 파일 경로(<tt>null</tt>이 될 수도 있음)
     * @return 확장자가 삭제된 파일의 경우 또는 파일이 없다면 <tt>null</tt>
     */
    public static String stripFilenameExtension(String path) {
        return org.springframework.util.StringUtils.stripFilenameExtension(path);
    }

    /**
     * Fully Qualified Path에서 마지막 Path Separator를 기준으로 좌측 경로를 반환한다.
     *
     * @param fullyQualifiedPath Fully Qualified Path
     * @return 마지막 Path Separator를 기준으로 좌측 경로
     */
    public static String getPath(String fullyQualifiedPath) {
        int sep = fullyQualifiedPath.lastIndexOf(SystemUtils.FILE_SEPARATOR);
        if (sep != 0) {
            return fullyQualifiedPath.substring(0, sep);
        }
        return SystemUtils.FILE_SEPARATOR;
    }

    /**
     * Fully Qualified Path에서 마지막 Path Separator를 기준으로 우측 경로를 반환한다.
     *
     * @param fullyQualifiedPath Fully Qualified Path
     * @return 마지막 Path Separator를 기준으로 우측 경로
     */
    public static String getDirectoryName(String fullyQualifiedPath) {
        int lastIndex = fullyQualifiedPath.lastIndexOf(SystemUtils.FILE_SEPARATOR);
        return fullyQualifiedPath.substring(lastIndex + 1);
    }

    /**
     * 파일시스템 경로에서 마지막에 위치한 구분자를 기준으로 우측 마지막 경로명을 반환한다.
     * ex. /home/cloudine/flamingo/web -> web
     * Path 문자열이 길 경우 getDirectoryName() 메소드 대신 사용 : String index out of range
     *
     * @param path 파일시스템 경로
     * @return 마지막 구분자를 기준으로 우측 경로
     */
    public static String getLastPathName(String path) {
        String separator = SystemUtils.FILE_SEPARATOR;
        return org.apache.commons.lang.StringUtils.substringAfterLast(path, separator);
    }

    /**
     * 피일시스템 경로에서 잘못된 문자열의 포함 여부를 검증한다.
     *
     * @param path 파일시스템 경로
     * @return true or false
     */
    public static boolean pathValidator(String path) {
        String[] invalidString = {"//", "/ /", "null"};
        for (String str : invalidString) {
            if (org.apache.commons.lang.StringUtils.contains(path, String.valueOf(str))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Fully Qualified Path에서 마지막 Path Separator를 기준으로 좌측 경로에 교체할 디렉토리 또는 파일명을 적용한다.
     *
     * @param fullyQualifiedPath Fully Qualified Path
     * @param replacement        교체할 파일명 또는 디렉토리명
     * @return 교체할 파일명 또는 디렉토리가 적용된 fully qualified path
     */
    public static String replaceLast(String fullyQualifiedPath, String replacement) {
        String path = getPath(fullyQualifiedPath);
        if (path.endsWith(SystemUtils.FILE_SEPARATOR)) {
            return path + replacement;
        }
        return path + SystemUtils.FILE_SEPARATOR + replacement;
    }

    /**
     * byte를 휴먼리더블 하게 변환
     * KiB는 2의 10승 즉 1024 byte를 의미한다.
     * @param bytes
     * @return
     */
    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
    /**
     * byte를 휴먼리더블 하게 변환
     * KB는  1000 byte를 의미한다.
     * @param bytes
     * @return
     */
    public static String humanReadableByte(Long bytes) {
        String cnt_size;
        Long size_b = bytes;
        Long size_kb = size_b /1024;
        Long size_mb = size_kb / 1024;
        Long size_gb = size_mb / 1024 ;

        if (size_gb > 0){
            cnt_size = size_gb + " GB";
        }else if(size_mb > 0){
            cnt_size = size_mb + " MB";
        }else if(size_kb > 0){
            cnt_size = size_kb + " KB";
        }else{
            cnt_size= size_b +" B";
        }
        return cnt_size;
    }
    /**
     * 디렉토리를 생성할 경우.
     */
    public static void createDir(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Could not create Directory!");
        }
    }
    /**
     * 심볼릭 링크를 생성할 경우
     */
    public static void createSymbolicLink(String link, String target) {
        try {
            Files.createSymbolicLink(Paths.get(link),Paths.get(target));
        } catch (IOException e) {
            throw new RuntimeException("Could not create symbolic link!");
        }
    }
    /**
     * 파일 내용 읽어 들이기
     */
    public static StringBuilder readFile(String filePath) {
        StringBuilder sb=new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(filePath));

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                sb.append(str);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not read file! ");
        }
        return sb;
    }

    /**
     * 주어진 path의 파일리스트를 리턴
     */
    public static List<FileData> listFile(String path) {
        File filepath = new File(path);
        List<FileData> fileList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        File [] files=null;
        //디렉토리
        if(filepath.isDirectory()){
            files=filepath.listFiles();
        }
        for(int i=0; i< files.length; i++) {
            FileData fd=FileData.builder()
                    .filename(files[i].getName())
                    .path(files[i].getPath())
                    .fileSize(FileUtils.humanReadableByte(files[i].length()))
                    .lastModifyDate(sdf.format(files[i].lastModified()))
                    .build();
            fileList.add(fd);
        }
        return fileList;
    }

}