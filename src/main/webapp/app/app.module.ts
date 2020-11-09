import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { KataSharedModule } from 'app/shared/shared.module';
import { KataCoreModule } from 'app/core/core.module';
import { KataAppRoutingModule } from './app-routing.module';
import { KataHomeModule } from './home/home.module';
import { KataEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    KataSharedModule,
    KataCoreModule,
    KataHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    KataEntityModule,
    KataAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class KataAppModule {}
