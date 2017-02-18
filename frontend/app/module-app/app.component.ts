import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/map';

import { Todo } from '../todo-data/todo';
import { TodoService } from '../todo-data/todo.service';

@Component({
	moduleId: module.id,
	selector: 'todo-app',
	templateUrl: 'app.component.html',
	providers: [TodoService]
})

export class AppComponent implements OnInit {
	todos: Todo[];
	lastRequestResult : boolean;
	nameToAddInput: string;
	
	edit : any = {
		editMode: <boolean> false,
		modifiedTodo: <Todo> new Todo()
	};
	
	todoWithSpecifiedNameAlreadyExists: boolean = false;
	
	constructor(private http: Http, private todoService: TodoService) { }

	ngOnInit(): void {
		this.getAllTodos();
	}
	
	getAllTodos() {
		this.todoService.getAllTodos()
		.subscribe(
			data => this.todos = data,
			err => this.logError(err),
			() => console.log("todo list obtained")
		);
	}
	
	addTodoClick(): void {
		this.todoWithSpecifiedNameAlreadyExists = false;
	
		var todo : Todo = new Todo();
		todo.name = this.nameToAddInput;
		
		this.todoService.addTodo(todo)
		.subscribe(
			data => {
				this.lastRequestResult = (data == "true");
				this.todoWithSpecifiedNameAlreadyExists = !this.lastRequestResult;
			},
			err => this.logError(err),
			() => {
				this.getAllTodos();
				console.log("add request result: "+ this.lastRequestResult);
			}
		);
	}
	
	enableEditModeTodoClick(todo:Todo){
		this.todoWithSpecifiedNameAlreadyExists = false;
	
		this.edit.editMode=true;
		this.edit.modifiedTodo.id = todo.id;
	}
	
	disableEditModeTodoClick(){
		this.todoWithSpecifiedNameAlreadyExists = false;
	
		this.edit.editMode=false;
		this.edit.modifiedTodo = new Todo();
	}
	
	saveEditedTodoClick(editedTodo: Todo): boolean{
		this.todoWithSpecifiedNameAlreadyExists = false;
	
		this.todoService.editTodo(editedTodo)
		.subscribe(
			data => {
				this.lastRequestResult = (data == "true");
				this.todoWithSpecifiedNameAlreadyExists = !this.lastRequestResult;
			},
			err => this.logError(err),
			() => {
				this.getAllTodos();
				if(!this.todoWithSpecifiedNameAlreadyExists) this.disableEditModeTodoClick();
				console.log("edit request result: "+ this.lastRequestResult);
			}
		);
		
		return false;
	}
	
	deleteTodoClick(id:number){
		this.todoService.deleteTodo(id)
		.subscribe(
			data => this.lastRequestResult = (data == "true"),
			err => this.logError(err),
			() => {
				this.getAllTodos();
				console.log("delete request result: " + this.lastRequestResult);
			}
		);
	}
	
	private logError(error: any): Promise<any> {
		console.error('An error occurred', error);
		return Promise.reject(error.message || error);
	}
  
}