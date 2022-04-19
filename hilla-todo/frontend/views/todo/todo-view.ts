import { Binder, field } from '@hilla/form';
import '@vaadin/icon';
import '@vaadin/icons';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-checkbox';
import '@vaadin/vaadin-messages';
import '@vaadin/vaadin-text-field';
import Todo from 'Frontend/generated/com/example/application/entity/Todo';
import TodoModel from 'Frontend/generated/com/example/application/entity/TodoModel';
import { TodoEndpoint } from 'Frontend/generated/endpoints';
import { html } from 'lit';
import { customElement, state } from 'lit/decorators.js';
import { View } from '../view';

@customElement('todo-view')
export class TodoView extends View {
  @state()
  private todos: Todo[] = [];
  private binder = new Binder(this, TodoModel);

  render() {
    return html`
      <div class="form">
        <vaadin-text-field ${field(this.binder.model.task)}> </vaadin-text-field>
        <vaadin-button theme="primary" @click=${this.createTodo} ?disabled=${this.binder.invalid}>
          Aggiungi
        </vaadin-button>
      </div>
      <div class="todos">
        <vaadin-list-box multiple .selectedValues="${[]}" style="height: 200px">
          ${this.todos.map(
            (task) =>
              html`
                <vaadin-item style="width:95%; display: inline-flex" .selected="${task.done}">
                  ${task.task}
                </vaadin-item>
                <vaadin-icon
                  @click="${async () => {
                    await TodoEndpoint.delete(task);
                    this.todos = (await TodoEndpoint.findAll()) as Todo[];
                  }}"
                  icon="vaadin:trash"
                ></vaadin-icon>
              `
          )}
        </vaadin-list-box>
      </div>
    `;
  }

  async connectedCallback() {
    super.connectedCallback(); //(1)
    // await TodoEndpoint.deleteAll(); // Clean all
    this.todos = (await TodoEndpoint.findAll()) as Todo[];
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
