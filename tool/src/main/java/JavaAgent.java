import adapters.ConstructSiteAdapter;
import adapters.FieldDetectionAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import system.SystemConfig;
import tools.InfoCollector;
import user.UserConfig;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

public class JavaAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        List<String> args = Arrays.asList(agentArgs.split(":"));
        if(args.size() < 2){
            System.out.println("ERROR agentArgs size! Expecting args size = 2,divided by :");
            return;
        }
        if(!args.get(1).equals("FieldDetection") && !args.get(1).equals("ConstructSite")){
            System.out.println("ERROR tool type! Expecting FieldDetection or ConstructSite");
            return;
        }
        UserConfig.getInstance().setUserConfigFilePath(args.get(0));
        UserConfig.getInstance().setMode(args.get(1));

        for(String arg: args){
            System.out.println(arg);
        }
        inst.addTransformer(new DefineTransformer(), true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run()
            {
                if(UserConfig.getInstance().getMode().equals("FieldDetection")){
                    InfoCollector.show(args.get(0));
                }
                else if(UserConfig.getInstance().getMode().equals("ConstructSite")){
                    InfoCollector.showConstructSite();
                }
            }
        }));
    }

    static class DefineTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (SystemConfig.getInstance().isInterestringClass(className)) {
                System.out.println(className);
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);

                //选择一个adpter去适配cr
                if(UserConfig.getInstance().getMode().equals("FieldDetection")){
                    FieldDetectionAdapter fda = new FieldDetectionAdapter(Opcodes.ASM8,cw);
                    cr.accept(fda,0);
                }
                else if(UserConfig.getInstance().getMode().equals("ConstructSite")){
                    ConstructSiteAdapter csa = new ConstructSiteAdapter(Opcodes.ASM8,cw);
                    cr.accept(csa,0);
                }
                else{
                    System.out.println("ERROR tool type! Expecting FieldDetection or ConstructSite");
                    return classfileBuffer;
                }


                return cw.toByteArray();
            }
            return classfileBuffer;
        }
    }
}
