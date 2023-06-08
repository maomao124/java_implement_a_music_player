package mao.impl;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import mao.MusicPlayer;
import mao.PlaybackType;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Project name(项目名称)：java实现音乐播放器
 * Package(包名): mao.impl
 * Class(类名): GoogleMp3spiMusicPlayerImpl
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/6/8
 * Time(创建时间)： 21:36
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class GoogleMp3spiMusicPlayerImpl implements MusicPlayer
{
    private static List<File> musicList = new ArrayList<>();

    private InputStream inputStream;

    /**
     * 播放位置，顺序播放和循环播放有用
     */
    private int location = -1;
    /**
     * 播放类型
     */
    private PlaybackType playbackType = PlaybackType.Sequential;
    private Player player;
    private Thread thread;

    @Override
    public void load()
    {
        load("./music");
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

    @Override
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
                else if (file1.getName().toLowerCase(Locale.ROOT).endsWith(".mp3"))
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

    @Override
    public void next()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (player != null)
                    {
                        close();
                    }
                    else
                    {
                        System.out.println("换歌线程已启动");
                        thread = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    if (player.isComplete())
                                    {
                                        next();
                                    }
                                }
                                catch (Exception e)
                                {
                                    //e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                    }
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
                    player = new Player(inputStream);
                    player.play();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
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

    @Override
    public void pauseOrStart()
    {
        throw new UnsupportedOperationException("暂未实现该功能");
    }

    @Override
    public boolean isPause()
    {
        throw new UnsupportedOperationException("暂未实现该功能");
    }

    @Override
    public void close()
    {
        //音乐停止播放
        System.out.println("音乐停止播放");
        player.close();
        thread.interrupt();
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
