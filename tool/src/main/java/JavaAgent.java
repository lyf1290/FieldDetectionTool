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

public class JavaAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        UserConfig.getInstance().setUserConfigFilePath(agentArgs);
        System.out.println("agentArgs : " + agentArgs);
        inst.addTransformer(new DefineTransformer(), true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                InfoCollector.show(agentArgs);
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

                // 选择一个adpter去适配cr
                FieldDetectionAdapter fda = new FieldDetectionAdapter(Opcodes.ASM8, cw);
                cr.accept(fda, 0);

                return cw.toByteArray();
            }
            return classfileBuffer;
        }
    }
}
