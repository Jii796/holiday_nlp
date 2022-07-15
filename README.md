### holiday_nlp
- 当前项目下面存在两个模型文件“classification-model_for4.ser”和“classification-model_for6.ser”
- 其中“for4”是针对emergency类型下的四类子数据进行训练，
“for6”是针对所有的数据进行训练
- 当前项目下的test文件夹下是十条验证数据
其中下载当前项目后，通过IDEA导入Hanlp-1.8.3，点击test/java/com/hankcs/demo/DemoTextClassification.java
修改其中的模型文件的地址为“classification-model_for4.ser”或“classification-model_for6.ser”两个文件所在地址
点击运行，即可对里面的数据进行文本分类，第一次运行耗时较长，因为会自动下载需要的data数据集
- 但是不能进行模型的评估，因为没有模型匹配的数据
