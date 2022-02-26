import java.util.HashMap;
import java.util.Map;

public class SymbolItem {
    interface Props {
        String IS_INIT = "isInit";
    }

    enum Kind {
        ATTR, LET_ID, CASE_ID, FORMAL_PARAM, METHOD, SELF
    }

    private final AbstractSymbol type;
    private Kind kind;
    private final Map<String, Object> properties = new HashMap<>();

    private Object value;

    public SymbolItem(Kind kind, AbstractSymbol type, Object value) {
        this.type = type;
        this.kind = kind;
        this.value = value;
    }

    public SymbolItem(Kind kind, AbstractSymbol type) {
        this(kind, type, null);
    }

    public SymbolItem(AbstractSymbol type, Object value) {
        this.type = type;
        this.value = value;
    }

    public AbstractSymbol getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Kind getKind() {
        return kind;
    }

    public Object getObject(String name) {
        return properties.get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean) properties.get(name);
    }

    public SymbolItem putBoolean(String name, Boolean value) {
        return putObject(name, value);
    }

    public SymbolItem putObject(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    public static SymbolItem newLetID(AbstractSymbol type) {
        return new SymbolItem(Kind.ATTR, type);
    }

    public static SymbolItem newSelf() {
        return new SymbolItem(Kind.SELF, TreeConstants.SELF_TYPE);
    }

    public static SymbolItem newAttr(attr attr) {
        return new SymbolItem(Kind.ATTR, attr.type_decl);
    }

    public static SymbolItem newMethod(method method) {
        return new SymbolItem(Kind.METHOD, method.return_type);
    }
}
