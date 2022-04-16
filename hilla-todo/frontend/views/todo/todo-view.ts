import { Binder, field } from '@hilla/form';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-checkbox';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-messages';
import { TodoEndpoint } from 'Frontend/generated/endpoints';
import { html } from 'lit';
import { customElement, state } from 'lit/decorators.js';
import { View } from '../view';
import TodoModel from 'Frontend/generated/com/example/application/entity/TodoModel';
import Todo from 'Frontend/generated/com/example/application/entity/Todo';

@customElement('todo-view')
export class TodoView extends View {
  @state()
  private todos: Todo[] = [];
  private binder = new Binder(this, TodoModel);

  render() {
    return html`
      <div class="form">
        <vaadin-text-field ${field(this.binder.model.task)}></vaadin-text-field>
        <vaadin-button theme="primary" @click=${this.createTodo} ?disabled=${this.binder.invalid}> Add </vaadin-button>
      </div>
      <div class="todos">
        ${this.todos.map(
          (todo) => html`
            <div class="todo">
              <vaadin-checkbox
                ?checked=${todo.done}
                @checked-changed=${(e: CustomEvent) => this.updateTodoState(todo, e.detail.value)}
              ></vaadin-checkbox>
              <span>${todo.task}</span>
            </div>
          `
        )}
        <vaadin-message-list .items="${this.todos.map((it) => Object.assign({ text: it.task }))}"></vaadin-message-list>
      </div>
    `;
  }

  async connectedCallback() {
    super.connectedCallback(); //(1)
    this.todos = await TodoEndpoint.findAll() as Todo[];
  }

  async createTodo() {
    const createdTodo = await this.binder.submitTo(TodoEndpoint.save);
    if (createdTodo) {
      this.todos = [...this.todos, createdTodo];
      this.binder.clear();
    }
  }

  updateTodoState(todo: Todo, done: boolean) {
    const updatedTodo = { ...todo, done };
    this.todos = this.todos.map((t) => (t.id === todo.id ? updatedTodo : t));
    TodoEndpoint.save(updatedTodo);
  }
}
