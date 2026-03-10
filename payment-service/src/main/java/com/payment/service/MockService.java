package com.payment.service;

import com.payment.dto.request.InitiatePaymentRequestDTO;

public class MockService {
    public static String getPaymentUrl(InitiatePaymentRequestDTO request) {

        return """
<!DOCTYPE html>
       <html>
       <head>
       <title>Secure Checkout</title>
       
       <style>
       
       body{
       font-family:Arial;
       background:#f4f6f8;
       }
       
       .container{
       width:500px;
       margin:50px auto;
       background:white;
       padding:30px;
       border-radius:10px;
       box-shadow:0 3px 10px rgba(0,0,0,0.1);
       }
       
       .tabs{
       display:flex;
       margin-bottom:20px;
       }
       
       .tab{
       flex:1;
       padding:10px;
       text-align:center;
       background:#eee;
       cursor:pointer;
       }
       
       .tab.active{
       background:#2a6df4;
       color:white;
       }
       
       .section{
       display:none;
       }
       
       .section.active{
       display:block;
       }
       
       input,select{
       width:100%;
       padding:10px;
       margin:8px 0;
       }
       
       button{
       width:100%;
       padding:12px;
       background:#2a6df4;
       color:white;
       border:none;
       font-size:16px;
       }
       
       .qr{
       text-align:center;
       padding:20px;
       }
       
       </style>
       
       <script>
       
       function showTab(id){
       
       document.querySelectorAll(".section")
       .forEach(e=>e.classList.remove("active"))
       
       document.querySelectorAll(".tab")
       .forEach(e=>e.classList.remove("active"))
       
       document.getElementById(id).classList.add("active")
       document.getElementById(id+"Tab").classList.add("active")
       
       }
       
       function showOtp(){
       
       document.getElementById("paymentSection").style.display="none"
       document.getElementById("otpSection").style.display="block"
       
       }
       
       function verifyOtp(){
       
       alert("Payment Successful")
       
       window.location.href="/payment/callback?status=SUCCESS"
       
       }
       
       </script>
       
       </head>
       
       <body>
       
       <div class="container" id="paymentSection">
       
       <h2>Secure Payment</h2>
       
       <div class="tabs">
       
       <div class="tab active" id="upiTab" onclick="showTab('upi')">
       UPI
       </div>
       
       <div class="tab" id="cardTab" onclick="showTab('card')">
       Card
       </div>
       
       <div class="tab" id="walletTab" onclick="showTab('wallet')">
       Wallet
       </div>
       
       </div>
       
       <!-- UPI -->
       
       <div class="section active" id="upi">
       
       <div class="qr">
       
       <img src="https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=upi://pay?pa=test@upi&pn=Merchant&am=500">
       
       <p>Scan QR using any UPI App</p>
       
       </div>
       
       <input placeholder="or Enter UPI ID">
       
       <button onclick="showOtp()">Pay with UPI</button>
       
       </div>
       
       <!-- CARD -->
       
       <div class="section" id="card">
       
       <input placeholder="Card Number">
       
       <input placeholder="Expiry MM/YY">
       
       <input placeholder="CVV">
       
       <button onclick="showOtp()">Pay with Card</button>
       
       </div>
       
       <!-- WALLET -->
       
       <div class="section" id="wallet">
       
       <select>
       
       <option>Paytm Wallet</option>
       <option>PhonePe Wallet</option>
       <option>Amazon Pay</option>
       
       </select>
       
       <button onclick="showOtp()">Pay with Wallet</button>
       
       </div>
       
       </div>
       
       <!-- OTP SCREEN -->
       
       <div class="container" id="otpSection" style="display:none">
       
       <h3>OTP Verification</h3>
       
       <p>Enter OTP sent to your phone</p>
       
       <input placeholder="Enter OTP">
       
       <button onclick="verifyOtp()">Verify OTP</button>
       
       </div>
       
       </body>
       </html>
""".formatted(request.getOrderId(), request.getAmount());
    }
    }