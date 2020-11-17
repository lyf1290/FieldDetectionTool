import adapters.FieldDetectionAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import system.SystemConfig;
import tools.ByteCodeTool;
import tools.InfoCollector;
import user.UserConfig;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM8;

//TODO localvariable,max还没修改，输入输出模块还没修改，
public class Main {
    private final static int ASM_VERSION = ASM8;

    public static void main(String args[]){
        Test test = new Test();
        test.getTest();
        test.getTest();
        new TestFather();
        InfoCollector.show();
//        byte[] sourceByteCodes = ByteCodeTool.input("target/classes/Test.class");
//
//        if(sourceByteCodes != null){
//            ClassReader cr = new ClassReader(sourceByteCodes);
//            ClassWriter cw = new ClassWriter(cr,ClassWriter.COMPUTE_MAXS);
//
//            //选择一个adpter去适配cr
//            FieldDetectionAdapter fda = new FieldDetectionAdapter(ASM8,cw);
//            cr.accept(fda,0);
//
//            ByteCodeTool.output(cw.toByteArray(),"output/Test.class");
//        }
//        sourceByteCodes = ByteCodeTool.input("target/classes/TestFather.class");
//
//        if(sourceByteCodes != null){
//            ClassReader cr = new ClassReader(sourceByteCodes);
//            ClassWriter cw = new ClassWriter(cr,ClassWriter.COMPUTE_MAXS);
//
//            //选择一个adpter去适配cr
//            FieldDetectionAdapter fda = new FieldDetectionAdapter(ASM8,cw);
//            cr.accept(fda,0);
//
//            ByteCodeTool.output(cw.toByteArray(),"output/TestFather.class");
//        }

    }
}

/*
* 需要一个全局的Adapter去delegates各个任务
* 1、需要一个专门增加Field的Adapter
* 2、需要一个增加Method的Adapter
* 3、需要一个增加Insn的Adapter
* 4、需要一个处理构造函数的AdviceAdapter
* */

//如果是被子类构造函数调用的父类构造函数，里面的this是指向子类的
//-javaagent:/Users/liangyufei/Desktop/tool/target/tool-1.0-SNAPSHOT.jar