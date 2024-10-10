package org.example.expert.domain.user.testdata;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenerateUserNickName {


    //

    /**
     * 랜덤 닉네임 생성법 1 (a~z로 8글자 생성)
     * @return ex) wqwlupkb
     */
    public static String generateRandomNickNameByAscii() {
        int leftLimit = 97; // 'a'
        int rightLimit = 122; // 'z'
        int targetStringLength = 8;
        Random random = new Random();

        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }


    /**
     * 랜덤 닉네임 생성법 2 (RandomStringUtils 이용) - build.gradle에 RandomStringUtils 의존성 추가 해야함
     * @return ex) sZ4WiDMi
     */
    public static String generateRandomNickNameByRandomStringUtils() {
        return RandomStringUtils.random(8, true, true); //
    }


    /**
     * 랜덤 닉네임 생성법 3 (Collection Shuffle 이용)
     * @return ex) 서운한 너구리 88300
     */
    public static String generateRandomNickNameByCollectionShuffle() {
        List<String> abj = Arrays.asList("화가 난", "신난", "즐거운", "슬픈", "몰두한", "울고 있는", "힘든", "외로운", "괴로운", "성실한", "감사한", "서운한");
        List<String> obj = Arrays.asList("사자", "호랑이", "코뿔소", "얼룩말", "강아지", "고양이", "토끼", "기니피그", "얼룩말", "너구리", "돌고래", "코끼리", "늑대", "여우");

        Random random = new Random();
        int num = random.nextInt(100000);
        String randomNum = String.valueOf(num);

        Collections.shuffle(abj);
        Collections.shuffle(obj);

        return abj.get(0) + " " + obj.get(0) + " " + randomNum;
    }

}
