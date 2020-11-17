package templates;


import adapters.FieldDetectionAdapter;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import models.FieldModel;
import models.MethodModel;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import system.SystemConfig;
import tools.InfoCollector;
import user.UserConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FieldDetectionTemplate extends Template {

    /**
     * 构造FieldDetection中对Field模版
     * @param owner 该field属于的className
     */
    private void buildFieldModels(String owner){
        List<FieldModel> fieldModels = new ArrayList<>();
        //如果这里改了，要到constructAdapter中进行对应的修改
        FieldModel fieldModel = new FieldModel(Opcodes.ACC_PUBLIC,"getDirtyTag","[I",null,null);
        fieldModels.add(fieldModel);
        fieldModel = new FieldModel(Opcodes.ACC_PUBLIC,"putDirtyTag","[I",null,null);
        fieldModels.add(fieldModel);
        this.newFieldModels.put(owner,fieldModels);
    }
    //TODO 对于没匹配到到字段，是不是应该调用外部类的相应函数
    /**
     * 构建modify方法的指令列表
     * modify(String fieldName){
     *  if(fieldName.equals("name")){
     *      .....
     *  }
     *  else if(fieldName.equals("id")){
     *      .....
     *  }
     * }
     * @return 指令列表
     */
    private InsnList buildPutGetFieldMethodInsnList(List<String> sourceFieldNameList,boolean putTag,String owner){
        /*
        !!!!!!!LineNumber一定要加上
        * */
        int nextLabelIndex = 0;
        int returnLabelIndex = -1;
        Label returnLabel;
        LabelNode returnLabelNode = null;
        int lineNum = 6;
        InsnList insnList = new InsnList();
        List<Label> labelList = new ArrayList<>();

        /*
        //如果putdirtytag没初始化，就先初始化，在先调用父类构造函数的时候会触发
        Label lBegin = new Label();
        labelList.add(lBegin);
        LabelNode lBeginNode = new LabelNode(lBegin);
        insnList.add(lBeginNode);
        lineNum += 3;
        insnList.add(new LineNumberNode(lineNum,lBeginNode));
        insnList.add(new VarInsnNode(Opcodes.ALOAD,0));
        if(putTag){
            insnList.add(new FieldInsnNode(Opcodes.GETFIELD,owner,"putDirtyTag","[I"));
        }
        else{
            insnList.add(new FieldInsnNode(Opcodes.GETFIELD,owner,"getDirtyTag","[I"));
        }
        Label notNullLabel = new Label();
        LabelNode notNullLabelNode = new LabelNode(notNullLabel);
        insnList.add(new JumpInsnNode(Opcodes.IFNONNULL,notNullLabelNode));
        Label nullLabel = new Label();
        LabelNode nullLabelNode = new LabelNode(nullLabel);
        insnList.add(nullLabelNode);
        insnList.add(new LineNumberNode(++lineNum,nullLabelNode));
        insnList.add(new VarInsnNode(Opcodes.ALOAD,0));
        insnList.add(new IntInsnNode(Opcodes.BIPUSH,sourceFieldNameList.size()));
        insnList.add(new IntInsnNode(Opcodes.NEWARRAY,Opcodes.T_INT));
        if(putTag){
            insnList.add(new FieldInsnNode(Opcodes.PUTFIELD,owner,"putDirtyTag","[I"));
        }
        else{
            insnList.add(new FieldInsnNode(Opcodes.PUTFIELD,owner,"getDirtyTag","[I"));
        }
        insnList.add(notNullLabelNode);
        lineNum += 2;
        insnList.add(new LineNumberNode(lineNum,notNullLabelNode));
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
         */

        //创建label0标志开始
        Label l0 = new Label();
        labelList.add(l0);
        //声明下一条指令代表着第一条语句
        insnList.add(new LabelNode(l0));
        LineNumberNode lineNumberNode = new LineNumberNode(lineNum,new LabelNode(l0));
        insnList.add(lineNumberNode);

        //将fieldName和该类原有的字段相比较
        for(int i = 0; i < sourceFieldNameList.size(); ++i){

            if(i != 0){
                //声明这个if-else语句是哪个Label
                LabelNode ifElseLabelNode = new LabelNode(labelList.get(nextLabelIndex));
                insnList.add(ifElseLabelNode);
                lineNum += 3;
                lineNumberNode = new LineNumberNode(lineNum,ifElseLabelNode);
                insnList.add(lineNumberNode);
                //修改Frame
                FrameNode frameNode = new FrameNode(Opcodes.F_SAME, 0, null, 0, null);
                insnList.add(frameNode);
            }

            //从局部变量表中取出fieldName  ALOAD 1
            VarInsnNode varInsnNode = new VarInsnNode(Opcodes.ALOAD,1);
            insnList.add(varInsnNode);
            //将要对比的字段的String放入到栈顶 Ldc sourceFieldName
            LdcInsnNode ldcInsnNode = new LdcInsnNode(sourceFieldNameList.get(i));
            insnList.add(ldcInsnNode);
            //调用equals方法进行对比 INVOKEVIRTUAL equals
            MethodInsnNode methodInsnNode = new MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/lang/String","equals", "(Ljava/lang/Object;)Z", false);
            insnList.add(methodInsnNode);
            //根据equals的结果判断控制流走向

            //下一个else if的label
            Label nextIfLabel = new Label();
            labelList.add(nextIfLabel);
            nextLabelIndex = labelList.size()-1;
            //根据equals的结果判断，如果否跳到下一个else if        IFEQ nextIfLabel
            JumpInsnNode jumpInsnNode = new JumpInsnNode(Opcodes.IFEQ,new LabelNode(nextIfLabel));
            insnList.add(jumpInsnNode);
            //根据equals的结果判断，如果是，执行以下操作

            //执行if内的动作
            //先添加label和声明label
            Label chooseLabel = new Label();
            LabelNode chooseLabelNode = new LabelNode(chooseLabel);
            labelList.add(chooseLabel);
            insnList.add(chooseLabelNode);
            lineNum++;
            lineNumberNode = new LineNumberNode(lineNum,chooseLabelNode);
            insnList.add(lineNumberNode);

            //if语句中执行的内容
            {
//                FieldInsnNode fieldInsnNode = new FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System", "out", "Ljava/io/PrintStream;");
//                insnList.add(fieldInsnNode);
//                insnList.add(new LdcInsnNode(sourceFieldNameList.get(i)));
//                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

                insnList.add(new VarInsnNode(Opcodes.ALOAD,0));
                if(putTag){
                    insnList.add(new FieldInsnNode(Opcodes.GETFIELD, owner, "putDirtyTag", "[I"));
                }
                else{
                    insnList.add(new FieldInsnNode(Opcodes.GETFIELD, owner, "getDirtyTag", "[I"));
                }
                insnList.add(new IntInsnNode(Opcodes.BIPUSH,i));
                insnList.add(new InsnNode(Opcodes.IALOAD));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                Label notEqualsLabel = new Label();
                LabelNode notEqualsLabelNode = new LabelNode(notEqualsLabel);
                labelList.add(notEqualsLabel);
                insnList.add(new JumpInsnNode(Opcodes.IF_ICMPNE,notEqualsLabelNode));
                Label equalsLabel = new Label();
                LabelNode equalsLabelNode = new LabelNode(equalsLabel);
                labelList.add(equalsLabel);
                insnList.add(equalsLabelNode);
                lineNum++;
                insnList.add(new LineNumberNode(lineNum,equalsLabelNode));
                insnList.add(new LdcInsnNode(owner));
                insnList.add(new LdcInsnNode(sourceFieldNameList.get(i)));
                insnList.add(new InsnNode(Opcodes.ICONST_0));
                if(putTag){
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tools/InfoCollector", "putField", "(Ljava/lang/String;Ljava/lang/String;Z)V", false));
                }
                else{
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tools/InfoCollector", "getField", "(Ljava/lang/String;Ljava/lang/String;Z)V", false));
                }
                if(returnLabelIndex == -1){
                    returnLabel = new Label();
                    labelList.add(returnLabel);
                    returnLabelNode = new LabelNode(returnLabel);
                    returnLabelIndex = labelList.size()-1;
                }
                insnList.add(new JumpInsnNode(Opcodes.GOTO,returnLabelNode));
                insnList.add(notEqualsLabelNode);
                lineNum += 3;
                insnList.add(new LineNumberNode(lineNum,notEqualsLabelNode));
                insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                insnList.add(new LdcInsnNode(owner));
                insnList.add(new LdcInsnNode(sourceFieldNameList.get(i)));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                if(putTag){
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tools/InfoCollector", "putField", "(Ljava/lang/String;Ljava/lang/String;Z)V", false));
                }
                else{
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tools/InfoCollector", "getField", "(Ljava/lang/String;Ljava/lang/String;Z)V", false));
                }
                Label modifyTagLabel = new Label();
                labelList.add(modifyTagLabel);
                LabelNode modifyTagLabelNode = new LabelNode(modifyTagLabel);
                insnList.add(modifyTagLabelNode);
                insnList.add(new LineNumberNode(++lineNum,modifyTagLabelNode));
                insnList.add(new VarInsnNode(Opcodes.ALOAD,0));
                if(putTag){
                    insnList.add(new FieldInsnNode(Opcodes.GETFIELD, owner, "putDirtyTag", "[I"));
                }
                else{
                    insnList.add(new FieldInsnNode(Opcodes.GETFIELD, owner, "getDirtyTag", "[I"));
                }
                insnList.add(new IntInsnNode(Opcodes.BIPUSH,i));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IASTORE));

            }



            if(returnLabelIndex == -1){
                returnLabel = new Label();
                labelList.add(returnLabel);
                returnLabelNode = new LabelNode(returnLabel);
                returnLabelIndex = labelList.size()-1;
            }
            insnList.add(new JumpInsnNode(Opcodes.GOTO,returnLabelNode));


        }
        if(sourceFieldNameList.size() != 0){
            //执行else内的动作
            //声明这个if-else语句是哪个Label
            LabelNode ifElseLabelNode = new LabelNode(labelList.get(nextLabelIndex));
            insnList.add(ifElseLabelNode);
            lineNum += 3;
            lineNumberNode = new LineNumberNode(lineNum,ifElseLabelNode);
            insnList.add(lineNumberNode);
            //修改Frame
            FrameNode frameNode = new FrameNode(Opcodes.F_SAME, 0, null, 0, null);
            insnList.add(frameNode);
            FieldInsnNode fieldInsnNode = new FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System", "out", "Ljava/io/PrintStream;");
            insnList.add(fieldInsnNode);
            insnList.add(new LdcInsnNode("Error: instrument error!"));
            insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

//            String outerClassName = SystemConfig.getInstance().getOuterClassName(owner);
//            if(outerClassName != null && SystemConfig.getInstance().isInterestringClass(outerClassName)){
//
//            }

            //最后一次循环，没有下一个else if
            if(returnLabelIndex == -1){
                returnLabel = new Label();
                labelList.add(returnLabel);
                returnLabelNode = new LabelNode(returnLabel);
                returnLabelIndex = labelList.size()-1;
            }
            //出了if语句，执行接下来的部分
            //声明接下来的return部分属于的label
            insnList.add(returnLabelNode);
            lineNum += 3;
            insnList.add(new LineNumberNode(lineNum,returnLabelNode));
            //增加frame
            insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        }

        //方法结束 要执行return   RETURN
        insnList.add(new InsnNode(Opcodes.RETURN));
        //修改局部变量表和max信息
        //创建代表着结尾的label
        Label lastLabel = new Label();
        labelList.add(lastLabel);
        //声明以下部分代表结尾
        insnList.add(new LabelNode(lastLabel));
        //想办法导出局部变量表和max信息
        return insnList;
    }

    /**
     * 构建FieldDetection中对Method的模版
     * @param sourceFieldNameList
     * @param owner
     */
    private void buildMethodModels(List<String> sourceFieldNameList,String owner){
        List<MethodModel> methodModels = new ArrayList<>();
        InsnList putFieldInsnList = buildPutGetFieldMethodInsnList(sourceFieldNameList,true,owner);
        InsnList getFieldInsnList = buildPutGetFieldMethodInsnList(sourceFieldNameList,false,owner);
        MethodModel methodModel = new MethodModel(Opcodes.ACC_PUBLIC,"getField","(Ljava/lang/String;)V",null,null,getFieldInsnList);
        methodModels.add(methodModel);
        methodModel = new MethodModel(Opcodes.ACC_PUBLIC,"putField","(Ljava/lang/String;)V",null,null,putFieldInsnList);
        methodModels.add(methodModel);

        this.newMethodModels.put(owner,methodModels);
    }



    public FieldDetectionTemplate(){
        List<String> classNameList = SystemConfig.getInstance().getInterestringClassNameList();
        for(String className : classNameList){
            List<String> fieldNameList = SystemConfig.getInstance().getFieldNameList(className);
            this.buildFieldModels(className);
            this.buildMethodModels(fieldNameList,className);
        }
    }

}

