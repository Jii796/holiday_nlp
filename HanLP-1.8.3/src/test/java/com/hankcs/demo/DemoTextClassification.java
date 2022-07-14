/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>me@hankcs.com</email>
 * <create-date>16/2/20 AM11:46</create-date>
 *
 * <copyright file="DemoAtFirstSight.java" company="码农场">
 * Copyright (c) 2008-2016, 码农场. All Right Reserved, http://www.hankcs.com/
 * This source is subject to Hankcs. Please contact Hankcs to get more information.
 * </copyright>
 */
package com.hankcs.demo;


import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.utility.TestUtility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 第一个demo,演示文本分类最基本的调用方式
 *
 * @author hankcs
 */
public class DemoTextClassification
{
    /**
     * 搜狗文本分类语料库5个类目，每个类目下1000篇文章，共计5000篇文章
     */
    public static final String CORPUS_FOLDER = TestUtility.ensureTestData("搜狗文本分类语料库迷你版", "http://file.hankcs.com/corpus/sogou-text-classification-corpus-mini.zip");
    /**
     * 模型保存路径
     */
    public static final String MODEL_PATH = "data/test/classification-model.ser";

    public static final String IN_FILE="D:\\holiday\\nlp\\Hanlp\\test.txt";

    public static void main(String[] args) throws IOException
    {
        IClassifier classifier = new NaiveBayesClassifier(trainOrLoadModel());

        predict(classifier,getContext(IN_FILE));
        predict(classifier, "10时57分我局接阳宗海公安分局报：在云南国土学院西门过去到马郎这段路上（高速路下面的桥下），看到有个男的从昨晚到现在一直睡在一张车上没动，可能是死掉了。\n" +
            "接警后、阳宗海分局指挥中心立即指令阳宗海七甸派出所民警前往调查处置，经七甸派出所民警到场后发现一辆云A6K3Y7号夏利牌小型轿车停放在路边，车内一名男子（姓名：陶勇，男，汉族，身份证号码：532425198112011814，户籍地址：云南省昆明市宜良县匡远街道办事处里仁社区居民委员会起春庙73号）经120现场确认死亡，现分局刑侦大队已到现场进行勘验，后续工作情况正在开展中。");
        predict(classifier, "某有限公司一名工人在作业工程中不幸遇难");
        predict(classifier, "2021年06月14日15时00分，接宜良县政府总值班室报：今日上午06时许，在宜良县南羊街道办事处上黄堡村路段，一辆 “铃木”牌二轮摩托车（车牌：云ADV006）侧翻出路面，导致驾驶员汪靖博（男，26岁）当场死亡。目前相关事故调查和善后工作正在有序开展。");
        predict(classifier, "昆明市自然灾害应急管理委员会办公室关于召开地震应急救援响应实战拉练准备会议的通知\n" +
            "<p>请及时签收，谢谢。<br/></p>");
        predict(classifier,"昆明市气象局、昆明市自然资源和规划局2022年6月13日11时15分联合继续发布地质灾害气象风险Ⅲ级预警：预计未来24小时昆明市主城区、东川、禄劝、寻甸、富民、石林、宜良、安宁、呈贡、嵩明、晋宁地质灾害气象风险等级为Ⅲ级（风险较高）。请注意加强防范。");
        predict(classifier,"东川区应急管理局截至目前未接到安全生产类和自然灾害类的情况报告。");
        predict(classifier,"7月12日上午11点，我局收到局属单位解放公园发来的火情报告：位于解放公园樱花坡木亭今晨发生火情，安保人员第一时间发现，并使用灭火器进行处置，同时拨打119，江岸区消防队到达现场进行处置，随即火被扑灭，无人员伤亡，木亭损失域3000-5000元，起火原因几责任正在调查中");
        predict(classifier,"安全生产");
    }

    private static void predict(IClassifier classifier, String text)
    {
        /*String result=classifier.classify(text);
        System.out.printf("《%s》 属于分类 【%s】\n", text,result);*/
        String[] result=classifier.classify(text,3,true);
        System.out.printf("《%s》 属于分类 【", text);
        for(int i=0;i< size(result);i++){
            System.out.printf("%s,",result[i]);
        }
        System.out.printf("】\n");
    }
    private static int size(String[] list){
        int result=0;
        for(int i=0;i<list.length;i++){
            if(list[i]!=null){
                result+=1;
            }
        }
        return result;
    }

    public static String getContext(String path){
        List<String> list = new ArrayList<String>();
        try
        {
            String encoding = "utf-8";
            File file = new File(path);
            if (file.isFile() && file.exists())
            { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("找不到指定的文件");
            }
        }
        catch (Exception e)
        {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        String result=list.toString();
        return result;
    }
    private static NaiveBayesModel trainOrLoadModel() throws IOException
    {
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(MODEL_PATH);
        if (model != null) return model;

        File corpusFolder = new File(CORPUS_FOLDER);
        if (!corpusFolder.exists() || !corpusFolder.isDirectory())
        {
            System.err.println("没有文本分类语料，请阅读IClassifier.train(java.lang.String)中定义的语料格式与语料下载：" +
                                   "https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E4%B8%8E%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90");
            System.exit(1);
        }

        IClassifier classifier = new NaiveBayesClassifier(); // 创建分类器，更高级的功能请参考IClassifier的接口定义
        classifier.train(CORPUS_FOLDER);                     // 训练后的模型支持持久化，下次就不必训练了
        model = (NaiveBayesModel) classifier.getModel();
        IOUtil.saveObjectTo(model, MODEL_PATH);
        return model;
    }
}
