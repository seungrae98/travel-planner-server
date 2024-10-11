package com.haandy.travel_planner_server.domain;

import java.io.File;

public class FileWatcher {
    private String filePath;
    private long timeout;   // 타임아웃 시간 (밀리초)
    private long interval;  // 확인 주기 (밀리초)

    public FileWatcher(String filePath, long timeout, long interval) {
        this.filePath = filePath;
        this.timeout = timeout;
        this.interval = interval;
    }

    public void deleteFileIfExists() {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println(filePath + "이(가) 삭제되었습니다.");
            } else {
                System.out.println("파일을 삭제하는 데 실패했습니다: " + filePath);
            }
        } else {
            System.out.println(filePath + "이(가) 존재하지 않습니다.");
        }
    }


    public boolean waitForFile() {
        long startTime = System.currentTimeMillis();
        File file = new File(filePath);

        while (System.currentTimeMillis() - startTime < timeout) {
            if (file.exists()) {
                return true;
            }
            try {
                Thread.sleep(interval); // interval 밀리초 동안 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false; // 타임아웃 후에도 파일이 생성되지 않음
    }
}
