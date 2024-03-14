import { Component, Inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OKTA_AUTH, OktaAuthStateService } from '@okta/okta-angular';
import { OktaAuth } from '@okta/okta-auth-js';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  isAuthenticated: boolean = false;
  userFullName: string = '';

  constructor(private router:Router,private oktaAuthService: OktaAuthStateService,
    @Inject(OKTA_AUTH) private oktaAuth: OktaAuth) { }

  ngOnInit(): void {
    //Subscribe to authenctication state changes
    this.oktaAuthService.authState$.subscribe(
      (result) => {
        this.isAuthenticated = result.isAuthenticated!;
      }
    );
  }

  doSearch(value: string){
    console.log(`value=${value}`);
    this.router.navigateByUrl(`/search/${value}`);
  }

}
