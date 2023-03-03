package com.codurance.training.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import com.codurance.training.tasks.impl.TaskList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.lang.System.lineSeparator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class ApplicationTest {
    public static final String PROMPT = "> ";
    private final PipedOutputStream inStream = new PipedOutputStream();
    private final PrintWriter inWriter = new PrintWriter(inStream, true);

    private final PipedInputStream outStream = new PipedInputStream();
    private final BufferedReader outReader = new BufferedReader(new InputStreamReader(outStream));

    private Thread applicationThread;

    public ApplicationTest() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new PipedInputStream(inStream)));
        PrintWriter out = new PrintWriter(new PipedOutputStream(outStream), true);
        TaskList taskList = new TaskList(in, out);
        applicationThread = new Thread(taskList);
    }

    @Before public void
    start_the_application() {
        applicationThread.start();
    }

    @After public void
    kill_the_application() throws IOException, InterruptedException {
        if (!stillRunning()) {
            return;
        }

        Thread.sleep(1000);
        if (!stillRunning()) {
            return;
        }

        applicationThread.interrupt();
        throw new IllegalStateException("The application is still running.");
    }

    @Test(timeout = 1000) public void
    it_works() throws IOException {
        execute("show");

        execute("add project secrets");
        execute("add task A1 secrets Eat more donuts.");
        execute("add task A2 secrets Destroy all humans.");

        execute("show");
        readLines(
            "secrets",
            "    [ ] A1: Eat more donuts.",
            "    [ ] A2: Destroy all humans.",
            ""
        );

        execute("add project training");
        execute("add task A3 training Four Elements of Simple Design");
        execute("add task A4 training SOLID");
        execute("add task A5 training Coupling and Cohesion");
        execute("add task A6 training Primitive Obsession");
        execute("add task A7 training Outside-In TDD");
        execute("add task A8 training Interaction-Driven Design");

        execute("check A1");
        execute("check A3");
        execute("check A5");
        execute("check A6");

        execute("show");
        readLines(
                "secrets",
                "    [x] A1: Eat more donuts.",
                "    [ ] A2: Destroy all humans.",
                "",
                "training",
                "    [x] A3: Four Elements of Simple Design",
                "    [ ] A4: SOLID",
                "    [x] A5: Coupling and Cohesion",
                "    [x] A6: Primitive Obsession",
                "    [ ] A7: Outside-In TDD",
                "    [ ] A8: Interaction-Driven Design",
                ""
        );

        execute("quit");
    }

    private void execute(String command) throws IOException {
        read(PROMPT);
        write(command);
    }

    private void read(String expectedOutput) throws IOException {
        int length = expectedOutput.length();
        char[] buffer = new char[length];
        outReader.read(buffer, 0, length);
        assertThat(String.valueOf(buffer), is(expectedOutput));
    }

    private void readLines(String... expectedOutput) throws IOException {
        for (String line : expectedOutput) {
            read(line + lineSeparator());
        }
    }

    private void write(String input) {
        inWriter.println(input);
    }

    private boolean stillRunning() {
        return applicationThread != null && applicationThread.isAlive();
    }
}
