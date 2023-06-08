package mao.impl;

import mao.MusicPlayer;
import mao.PlaybackType;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Project name(项目名称)：java实现音乐播放器
 * Package(包名): mao.impl
 * Class(类名): JdkMusicPlayerImpl
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/6/8
 * Time(创建时间)： 20:08
 * Version(版本): 1.0
 * Description(描述)： jdk自带的播放器，不支持mp3格式
 */

public class JdkMusicPlayerImpl implements MusicPlayer
{
    private static List<File> musicList = new ArrayList<>();

    private volatile Clip clip;
    private volatile AudioInputStream audioInputStream;
    private volatile BufferedInputStream bufferedInputStream;
    private volatile InputStream inputStream;
    private LineListener lineListener;
    /**
     * 播放位置，顺序播放和循环播放有用
     */
    private int location = -1;
    /**
     * 播放类型
     */
    private PlaybackType playbackType = PlaybackType.Sequential;

    /**
     * 得到int随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return int
     */
    private static int getIntRandom(int min, int max)
    {
        if (min > max)
        {
            min = max;
        }
        return min + (int) (Math.random() * (max - min + 1));
    }


    /**
     * 加载音乐到musicList
     */
    public void load()
    {
        load("./music");
    }

    /**
     * 加载音乐到musicList
     *
     * @param path 音乐路径
     */
    public void load(String path)
    {
        try
        {
            File file = new File(path);
            //读取音乐列表
            File[] files = file.listFiles();
            //过滤文件类型，目前只支持wav格式
            for (File file1 : files)
            {
                if (file1.getName().toLowerCase(Locale.ROOT).endsWith(".wav"))
                {
                    //添加到列表
                    musicList.add(file1);
                    System.out.println("音乐 \"" + file1.getName() + "\"添加到播放列表");
                }
            }
            System.out.println("播放列表：" + musicList);
        }
        catch (Exception e)
        {
            System.out.println("加载音乐列表时出现异常");
            e.printStackTrace();
        }
    }


    /**
     * 随机得到一个音乐文件对象，随机播放的实现
     *
     * @return {@link File}
     */
    private File getRandomMusicFile()
    {
        if (musicList.size() == 0)
        {
            System.out.println("没有音乐可以播放！");
            return null;
        }
        location = getIntRandom(0, musicList.size() - 1);
        return musicList.get(location);
    }

    /**
     * 开始播放或者播放下一曲
     */
    public synchronized void next()
    {
        //背景音乐
        try
        {
            if (clip != null)
            {
                if (clip.isRunning())
                {
                    System.out.println("当前音乐正在播放");
                    close();
                }
            }
            nextMusic();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void previous()
    {
        throw new UnsupportedOperationException("暂未实现该功能");
    }

    @Override
    public PlaybackType getPlaybackType()
    {
        return this.playbackType;
    }

    @Override
    public void setPlaybackType(PlaybackType playbackType)
    {
        this.playbackType = playbackType;
    }

    private synchronized void nextMusic()
    {
        try
        {
            File file = null;
            if (playbackType == PlaybackType.shuffle)
            {
                //随机播放
                file = getRandomMusicFile();
            }
            else if (playbackType == PlaybackType.loop)
            {
                //循环顺序播放
                //是否为最后一曲
                if (location == musicList.size() - 1)
                {
                    location = 0;
                }
                else
                {
                    location++;
                }
                file = musicList.get(location);
            }
            else
            {
                //System.out.println(location);
                //System.out.println(musicList.size());
                //顺序播放
                //是否为最后一曲
                if (location == musicList.size() - 1)
                {
                    //播放完毕
                    System.out.println("音乐播放完毕");
                }
                else
                {
                    location++;
                    file = musicList.get(location);
                }
            }
            if (file == null)
            {
                return;
            }
            System.out.println("开始播放：" + file.getName());
            inputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(inputStream);
            audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            lineListener = new LineListener()
            {
                @Override
                public void update(LineEvent event)
                {
                    System.out.println("音乐播放事件：" + event.getType());
                    if (event.getType() == LineEvent.Type.STOP)
                    {
                        close();
                        nextMusic();
                    }
                }
            };
            clip.addLineListener(lineListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 暂停或开始播放音乐
     */
    public synchronized void pauseOrStart()
    {
        if (clip == null)
        {
            return;
        }
        if (clip.isRunning())
        {
            //当前为播放状态
            clip.removeLineListener(lineListener);
            clip.stop();
            clip.addLineListener(lineListener);
            System.out.println("音乐暂停播放");
        }
        else
        {
            //当前为暂停状态
            try
            {
                clip.start();
                System.out.println("音乐开始播放");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isPause()
    {
        return !clip.isRunning();
    }

    /**
     * 停止音乐的播放
     */
    public synchronized void close()
    {
        //音乐停止播放
        System.out.println("音乐停止播放");
        clip.removeLineListener(lineListener);
        clip.close();
        try
        {
            audioInputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            bufferedInputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
