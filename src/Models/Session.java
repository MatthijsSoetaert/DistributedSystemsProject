package Models;

public class Session {
    private String symKey;
    private String tag;
    private int index;
    private String name;

    //For testing
    private String nextTag;
    private int nextIndex;

    public String getNextTag() {
        return nextTag;
    }

    public void setNextTag(String nextTag) {
        this.nextTag = nextTag;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
    //End testing

    public Session() {
    }


    public Session(String symKey, String tag, int index, String name) {
        this.symKey = symKey;
        this.tag = tag;
        this.index = index;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymKey() {
        return symKey;
    }

    public void setSymKey(String symKey) {
        this.symKey = symKey;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
