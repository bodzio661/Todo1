package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Todo1App;
import com.mycompany.myapp.domain.Todo;
import com.mycompany.myapp.repository.TodoRepository;
import com.mycompany.myapp.repository.search.TodoSearchRepository;
import com.mycompany.myapp.service.TodoService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TodoResource} REST controller.
 */
@SpringBootTest(classes = Todo1App.class)
public class TodoResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DONE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DONE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_DONE = false;
    private static final Boolean UPDATED_IS_DONE = true;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.TodoSearchRepositoryMockConfiguration
     */
    @Autowired
    private TodoSearchRepository mockTodoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTodoMockMvc;

    private Todo todo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TodoResource todoResource = new TodoResource(todoService);
        this.restTodoMockMvc = MockMvcBuilders.standaloneSetup(todoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todo createEntity(EntityManager em) {
        Todo todo = new Todo()
            .description(DEFAULT_DESCRIPTION)
            .username(DEFAULT_USERNAME)
            .doneDate(DEFAULT_DONE_DATE)
            .isDone(DEFAULT_IS_DONE);
        return todo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todo createUpdatedEntity(EntityManager em) {
        Todo todo = new Todo()
            .description(UPDATED_DESCRIPTION)
            .username(UPDATED_USERNAME)
            .doneDate(UPDATED_DONE_DATE)
            .isDone(UPDATED_IS_DONE);
        return todo;
    }

    @BeforeEach
    public void initTest() {
        todo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTodo() throws Exception {
        int databaseSizeBeforeCreate = todoRepository.findAll().size();

        // Create the Todo
        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(todo)))
            .andExpect(status().isCreated());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeCreate + 1);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTodo.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTodo.getDoneDate()).isEqualTo(DEFAULT_DONE_DATE);
        assertThat(testTodo.isIsDone()).isEqualTo(DEFAULT_IS_DONE);

        // Validate the Todo in Elasticsearch
        verify(mockTodoSearchRepository, times(1)).save(testTodo);
    }

    @Test
    @Transactional
    public void createTodoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = todoRepository.findAll().size();

        // Create the Todo with an existing ID
        todo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(todo)))
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Todo in Elasticsearch
        verify(mockTodoSearchRepository, times(0)).save(todo);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setDescription(null);

        // Create the Todo, which fails.

        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(todo)))
            .andExpect(status().isBadRequest());

        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setUsername(null);

        // Create the Todo, which fails.

        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(todo)))
            .andExpect(status().isBadRequest());

        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoneDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setDoneDate(null);

        // Create the Todo, which fails.

        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(todo)))
            .andExpect(status().isBadRequest());

        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setIsDone(null);

        // Create the Todo, which fails.

        restTodoMockMvc.perform(post("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(todo)))
            .andExpect(status().isBadRequest());

        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTodos() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todoList
        restTodoMockMvc.perform(get("/api/todos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].doneDate").value(hasItem(DEFAULT_DONE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isDone").value(hasItem(DEFAULT_IS_DONE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", todo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(todo.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.doneDate").value(DEFAULT_DONE_DATE.toString()))
            .andExpect(jsonPath("$.isDone").value(DEFAULT_IS_DONE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTodo() throws Exception {
        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodo() throws Exception {
        // Initialize the database
        todoService.save(todo);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTodoSearchRepository);

        int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Update the todo
        Todo updatedTodo = todoRepository.findById(todo.getId()).get();
        // Disconnect from session so that the updates on updatedTodo are not directly saved in db
        em.detach(updatedTodo);
        updatedTodo
            .description(UPDATED_DESCRIPTION)
            .username(UPDATED_USERNAME)
            .doneDate(UPDATED_DONE_DATE)
            .isDone(UPDATED_IS_DONE);

        restTodoMockMvc.perform(put("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTodo)))
            .andExpect(status().isOk());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodo.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTodo.getDoneDate()).isEqualTo(UPDATED_DONE_DATE);
        assertThat(testTodo.isIsDone()).isEqualTo(UPDATED_IS_DONE);

        // Validate the Todo in Elasticsearch
        verify(mockTodoSearchRepository, times(1)).save(testTodo);
    }

    @Test
    @Transactional
    public void updateNonExistingTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Create the Todo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodoMockMvc.perform(put("/api/todos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(todo)))
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Todo in Elasticsearch
        verify(mockTodoSearchRepository, times(0)).save(todo);
    }

    @Test
    @Transactional
    public void deleteTodo() throws Exception {
        // Initialize the database
        todoService.save(todo);

        int databaseSizeBeforeDelete = todoRepository.findAll().size();

        // Delete the todo
        restTodoMockMvc.perform(delete("/api/todos/{id}", todo.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Todo in Elasticsearch
        verify(mockTodoSearchRepository, times(1)).deleteById(todo.getId());
    }

    @Test
    @Transactional
    public void searchTodo() throws Exception {
        // Initialize the database
        todoService.save(todo);
        when(mockTodoSearchRepository.search(queryStringQuery("id:" + todo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(todo), PageRequest.of(0, 1), 1));
        // Search the todo
        restTodoMockMvc.perform(get("/api/_search/todos?query=id:" + todo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].doneDate").value(hasItem(DEFAULT_DONE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isDone").value(hasItem(DEFAULT_IS_DONE.booleanValue())));
    }
}
