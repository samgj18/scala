package core.advanced.lectures

object ThreadModelLimitation extends App {
  // 1. OOP encapsulation is only valid in the SINGLE THREADED MODEL
  class BankAccount(private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int): Unit = this.amount -= money

    def safeWithdraw(money: Int): Unit = this.synchronized {
      this.amount -= money
    }

    def deposit(money: Int): Unit = this.amount += money

    def safeDeposit(money: Int): Unit = this.synchronized {
      this.amount += money
    }

    def getAmount: Int = amount

  }

  val account = new BankAccount(2000)
  for (_ <- 1 to 1000) {
    new Thread(() => account.withdraw(1)).start()
  }

  for (_ <- 1 to 1000) {
    new Thread(() => account.deposit(1)).start()
  }

  // This will NOT be 2000
  println(account.getAmount)

  // OOP encapsulation is broken in a multithreaded environment
  // Synchronization! Locks to the rescue
  //
  // Deadlocks, livelocks, and starvation

  // 2. Delegating something to a thread is a PAIN
  // you have a running thread and you want to pass a runnable to that thread
  var task: Runnable        = null
  val runningThread: Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for a task...")
          runningThread.wait()
        }
      }

      task.synchronized {
        println("[background] I have a task")
        task.run()
        task = null
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable): Unit = {
    if (task == null) task = r

    runningThread.synchronized {
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(500)
  delegateToBackgroundThread(() => println(42))
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println("This should run in the background"))

  // 3. Tracing and dealing with errors in a multithreaded environment is a PITN
}
