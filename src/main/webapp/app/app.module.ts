import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Todo1SharedModule } from 'app/shared/shared.module';
import { Todo1CoreModule } from 'app/core/core.module';
import { Todo1AppRoutingModule } from './app-routing.module';
import { Todo1HomeModule } from './home/home.module';
import { Todo1EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    Todo1SharedModule,
    Todo1CoreModule,
    Todo1HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Todo1EntityModule,
    Todo1AppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class Todo1AppModule {}
