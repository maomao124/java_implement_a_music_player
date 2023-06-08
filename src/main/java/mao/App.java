package mao;

import mao.impl.GoogleMp3spiMusicPlayerImpl;
import mao.impl.JdkMusicPlayerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Project name(项目名称)：java实现音乐播放器
 * Package(包名): mao
 * Class(类名): App
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/6/8
 * Time(创建时间)： 20:38
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class App
{
    public static void main(String[] args)
    {
        //MusicPlayer musicPlayer = new JdkMusicPlayerImpl();
        MusicPlayer musicPlayer = new GoogleMp3spiMusicPlayerImpl();
        musicPlayer.load();

        JFrame jFrame = new JFrame("音乐播放器");
        jFrame.setSize(1280, 600);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        jFrame.setLocation(screenWidth / 2 - jFrame.getWidth() / 2, screenHeight / 2 - jFrame.getHeight() / 2);  //位于屏幕中央
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        jFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("关闭");
                try
                {
                    musicPlayer.close();
                }
                catch (Exception ignored)
                {
                }
                System.exit(1);
            }
        });

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 3));

        Font font = new Font("宋体", Font.BOLD, 38);

        JButton jButton1 = new JButton("上一曲");
        JButton jButton2 = new JButton("播放");
        JButton jButton3 = new JButton("下一曲");
        JButton jButton4 = new JButton("当前：随机播放");
        jButton1.setFont(font);
        jButton2.setFont(font);
        jButton3.setFont(font);
        jButton4.setFont(font);
        jButton1.setBackground(Color.green);
        jButton2.setBackground(Color.green);
        jButton3.setBackground(Color.green);
        jButton4.setBackground(Color.green);
        jButton1.setForeground(Color.MAGENTA);
        jButton2.setForeground(Color.MAGENTA);
        jButton3.setForeground(Color.MAGENTA);
        jButton4.setForeground(Color.MAGENTA);


        jButton1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    musicPlayer.previous();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        jButton2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    musicPlayer.pauseOrStart();

                    try
                    {
                        musicPlayer.isPause();
                    }
                    catch (NullPointerException ex)
                    {
                        musicPlayer.next();
                    }
                    if (musicPlayer.isPause())
                    {
                        jButton2.setText("播放");
                    }
                    else
                    {
                        jButton2.setText("暂停");
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        jButton3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                musicPlayer.next();
            }
        });

        jButton4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                PlaybackType playbackType = musicPlayer.getPlaybackType();
                if (playbackType == PlaybackType.shuffle)
                {
                    musicPlayer.setPlaybackType(PlaybackType.Sequential);
                    jButton4.setText("当前：顺序播放");
                }
                else if (playbackType == PlaybackType.Sequential)
                {
                    musicPlayer.setPlaybackType(PlaybackType.loop);
                    jButton4.setText("当前：循环播放");
                }
                else if (playbackType == PlaybackType.loop)
                {
                    musicPlayer.setPlaybackType(PlaybackType.shuffle);
                    jButton4.setText("当前：随机播放");
                }
            }
        });

        jPanel.add(jButton1);
        jPanel.add(jButton2);
        jPanel.add(jButton3);
        jPanel.add(jButton4);

        jFrame.add(jPanel);

        jFrame.setVisible(true);
    }
}
