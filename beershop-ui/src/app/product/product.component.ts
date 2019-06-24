import { Component, OnInit } from '@angular/core';
import { ProductService } from "./product.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {

  products: Object;

  createProductForm: FormGroup;
  submitted = false;

  constructor(private productService: ProductService,
              private formBuilder: FormBuilder) {
    this.createProductForm = this.formBuilder.group({
      name: ['', Validators.required],
      price: ['', Validators.required],
      alcoholPercentage: ['', Validators.required],
      quantity: ['', Validators.required],
      weight: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.updateProductList();
  }

  onCreateProduct() {

    this.submitted = true;

    if (this.createProductForm.invalid) {
      return;
    }

    this.productService.createProduct({
      'name': this.createProductForm.value.name,
      'price': this.createProductForm.value.price,
      'alcoholPercentage': this.createProductForm.value.alcoholPercentage,
      'quantity': this.createProductForm.value.quantity,
      'weight': {
        'amount': this.createProductForm.value.weight,
        'unit': 'GRAM'
      }
    }).subscribe(() => {
      this.updateProductList();
      this.clearCreateProductForm();
    });
  }

  private clearCreateProductForm() {
    this.createProductForm.reset();
    this.submitted = false;
  }

  private updateProductList() {
    this.productService.getProducts().subscribe(data => {
      this.products = data;
    })
  }
}
