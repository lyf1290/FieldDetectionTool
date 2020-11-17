package templates;

import models.FieldModel;
import models.MethodModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对于孤立的部分的添加，比如新的Field或新的method可以使用模版
 */
public class Template {

    protected Map<String,List<FieldModel>> newFieldModels = new HashMap<>();
    protected Map<String,List<MethodModel>> newMethodModels = new HashMap<>();

    public List<FieldModel> getNewFieldInfos(String owner) {
        return newFieldModels.get(owner);
    }

    public List<MethodModel> getNewMethodInfos(String owner) {
        return newMethodModels.get(owner);
    }

    public void setNewFieldInfos(Map<String,List<FieldModel>> newFieldModels) {
        this.newFieldModels = newFieldModels;
    }

    public void setNewMethodInfos(Map<String,List<MethodModel>> newMethodModels) {
        this.newMethodModels = newMethodModels;
    }
}
