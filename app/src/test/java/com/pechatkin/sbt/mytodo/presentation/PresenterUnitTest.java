package com.pechatkin.sbt.mytodo.presentation;

import androidx.annotation.NonNull;

import com.pechatkin.sbt.mytodo.data.model.Task;
import com.pechatkin.sbt.mytodo.data.repository.TaskRepository;
import com.pechatkin.sbt.mytodo.presentation.presenter.TaskPresenter;
import com.pechatkin.sbt.mytodo.presentation.view.ITaskView;
import com.pechatkin.sbt.mytodo.presentation.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PresenterUnitTest {

    @Mock
    private ITaskView mITaskView;
    @Mock
    private TaskRepository mTaskRepository;

    private TaskPresenter mTaskPresenter;

    @Before
    public void setUp() {
        mTaskPresenter = new TaskPresenter(mITaskView, mTaskRepository);
    }

    @Test
    public void testDetachView() {
        mTaskPresenter.detachView();

        mITaskView.showData(createTestTaskList());

        verifyNoMoreInteractions(mTaskRepository);
    }

    @Test
    public void testDeleteTask() {

        mTaskPresenter.deleteTask("test", 0);

        InOrder inOrder = Mockito.inOrder(mITaskView, mTaskRepository);
        inOrder.verify(mITaskView).deleteTask("test", 0);
        inOrder.verify(mTaskRepository).deleteTask("test");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testUpdateTask() {

        mTaskPresenter.updateTask("test", 1);

        InOrder inOrder = Mockito.inOrder(mITaskView, mTaskRepository);
        inOrder.verify(mITaskView).updateTask("test", 1);
        inOrder.verify(mTaskRepository).updateTask("test");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testAddTask() {
        mTaskPresenter.addTask("test");

        InOrder inOrder = Mockito.inOrder(mITaskView, mTaskRepository);
        inOrder.verify(mTaskRepository).insertTask("test");
        inOrder.verify(mITaskView).addTask("test");
        inOrder.verifyNoMoreInteractions();

    }

    @Test
    public void testLoadTasks() {
        final List<Task> taskList = createTestTaskList();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {

                TaskRepository.OnLoadingFinishListener onLoadingFinishListener = (TaskRepository.OnLoadingFinishListener) invocation.getArguments()[0];
                onLoadingFinishListener.onFinish(taskList);
                return null;
            }
        }).when(mTaskRepository).loadData(any(TaskRepository.OnLoadingFinishListener.class));

        mTaskPresenter.loadTasks();
        InOrder inOrder = Mockito.inOrder(mITaskView);
        inOrder.verify(mITaskView).showProgress();
        inOrder.verify(mITaskView).hideProgress();
        inOrder.verify(mITaskView).showData(taskList);
        inOrder.verifyNoMoreInteractions();
    }

    private List<Task> createTestTaskList() {
        List<Task> testData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testData.add(new Task(""+ i, 0));
        }
        return testData;
    }
}
