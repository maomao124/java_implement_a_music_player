package mao;

import mao.impl.JdkMusicPlayerImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

/**
 * Project name(项目名称)：java实现音乐播放器
 * Package(包名): mao
 * Class(类名): Test
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/6/8
 * Time(创建时间)： 20:37
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class Test
{
    public static void main(String[] args)
    {
        MusicPlayer musicPlayer = new JdkMusicPlayerImpl();
        musicPlayer.load();
        musicPlayer.next();
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }
}
