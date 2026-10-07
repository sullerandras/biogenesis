package biogenesis.clade_analyzer.db;

import java.sql.SQLException;

public abstract class Job<T> {
  private final Promise<T> promise;

  public Job(Promise<T> promise) {
    this.promise = promise;
    JobManager.addJob(this);
  }

  void runJob() {
    try {
      T result = Job.this.run();
      promise.deliverResult(result);
    } catch (SQLException|RuntimeException e) {
      promise.deliverException(e);
    }
  }

  public abstract T run() throws SQLException;
}
