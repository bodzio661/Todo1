package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.TodoService;
import com.mycompany.myapp.domain.Todo;
import com.mycompany.myapp.repository.TodoRepository;
import com.mycompany.myapp.repository.search.TodoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Todo}.
 */
@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    private final Logger log = LoggerFactory.getLogger(TodoServiceImpl.class);

    private final TodoRepository todoRepository;

    private final TodoSearchRepository todoSearchRepository;

    public TodoServiceImpl(TodoRepository todoRepository, TodoSearchRepository todoSearchRepository) {
        this.todoRepository = todoRepository;
        this.todoSearchRepository = todoSearchRepository;
    }

    /**
     * Save a todo.
     *
     * @param todo the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Todo save(Todo todo) {
        log.debug("Request to save Todo : {}", todo);
        Todo result = todoRepository.save(todo);
        todoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the todos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Todo> findAll(Pageable pageable) {
        log.debug("Request to get all Todos");
        return todoRepository.findAll(pageable);
    }

    /**
     * Get one todo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Todo> findOne(Long id) {
        log.debug("Request to get Todo : {}", id);
        return todoRepository.findById(id);
    }

    /**
     * Delete the todo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Todo : {}", id);
        todoRepository.deleteById(id);
        todoSearchRepository.deleteById(id);
    }

    /**
     * Search for the todo corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Todo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Todos for query {}", query);
        return todoSearchRepository.search(queryStringQuery(query), pageable);    }
}
