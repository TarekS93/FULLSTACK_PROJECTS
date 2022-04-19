import { Commands, Context, Route } from '@vaadin/router';
import { isLoggedIn } from './auth';
import './views/login/login-view';
import './views/main-layout';
import './views/todo/todo-view';

export type ViewRoute = Route & {
  title?: string;
  icon?: string;
  children?: ViewRoute[];
};

const actionGuard = (_: Context, commands: Commands) => {
  // if (!isLoggedIn()) {
  //   return commands.redirect('/login');
  // }
  // return undefined;
};

export const views: ViewRoute[] = [
  // place routes below (more info https://hilla.dev/docs/routing)
  {
    path: '',
    action: actionGuard,
    component: 'todo-view',
    icon: '',
    title: '',
  },
  {
    path: 'todo',
    action: actionGuard,
    component: 'todo-view',
    icon: 'la la-list-alt',
    title: 'Todo',
  },
];
export const routes: ViewRoute[] = [
  {
    path: '/main',
    component: 'main-layout',
  },
  {
    path: '',
    component: 'main-layout',
    action: actionGuard,
    children: [...views],
  },
];
