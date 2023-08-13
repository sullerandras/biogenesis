package biogenesis.clade_analyzer.db;

public class Promise<T> {
  public interface SuccessHandler<T> {
    void onSuccess(T result);
  }

  public interface ErrorHandler {
    void onError(Exception e);
  }

  private SuccessHandler<T> successHandler;
  private ErrorHandler errorHandler;

  Promise() {
  }

  void deliverResult(T result) {
    if (successHandler != null) {
      successHandler.onSuccess(result);
    }
  }

  void deliverException(Exception e) {
    if (errorHandler != null) {
      errorHandler.onError(e);
    }
  }

  public Promise<T> then(SuccessHandler<T> successHandler) {
    this.successHandler = successHandler;
    return this;
  }

  public Promise<T> onError(ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
    return this;
  }
}
