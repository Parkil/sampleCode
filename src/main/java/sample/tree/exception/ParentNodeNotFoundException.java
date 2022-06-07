package sample.tree.exception;

public class ParentNodeNotFoundException extends RuntimeException{
    public ParentNodeNotFoundException(String msg) {
        super(msg);
    }
}
