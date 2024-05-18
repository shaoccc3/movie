//package com.ispan.theater.opay;
//
//public class Opay {
//
//	public static void main(String[] args) {
//		/* * 產生訂單的範例程式碼。 */
//		List<String> enErrors = new ArrayList<String>();
//		try {
//			AllInOne oPayment = new AllInOne();
//			/* 服務參數 */ oPayment.ServiceMethod = HttpMethod.HttpPOST;
//			oPayment.ServiceURL = "<<您要呼叫的服務位址>>";
//			oPayment.HashKey = "<<O’Pay提供給您的Hash Key>>";
//			oPayment.HashIV = "<<O’Pay提供給您的Hash IV>>";
//			oPayment.MerchantID = "<<O’Pay提供給您的特店編號>>";
//			/* 基本參數 */
//			oPayment.Send.ReturnURL = "<<您要收到付款完成通知的伺服器端網址>>";
//			oPayment.Send.ClientBackURL = "<<您要歐付寶返回按鈕導向的瀏覽器端網址>>";
//			oPayment.Send.OrderResultURL = "<<您要收到付款完成通知的瀏覽器端網址>>";
//			oPayment.Send.MerchantTradeNo = "<<您此筆訂單交易編號>>";
//			oPayment.Send.MerchantTradeDate = new Date();
//			// "<<您此筆訂單的交易時間>>"
//			oPayment.Send.TotalAmount = new Decimal("<<您此筆訂單的交易總金額>>");
//			oPayment.Send.TradeDesc = "<<您該筆訂單的描述>>";
//			oPayment.Send.ChoosePayment = PaymentMethod.ALL;
//			oPayment.Send.Remark = "<<您要填寫的其他備註>>";
//			oPayment.Send.ChooseSubPayment = PaymentMethodItem.None;
//			oPayment.Send.NeedExtraPaidInfo = ExtraPaymentInfo.No;
//			oPayment.Send.DeviceSource = DeviceType.PC;
//			oPayment.Send.IgnorePayment = "<<您不要顯示的付款方式>>";
//			// 例如財付通):Tenpay;
//			oPayment.Send.ChooseUseRedeem = UseRedeem.Yes;
//			// 購物金折抵
//			// 加入選購商品資料。
//			Item a1 = new Item();
//			a1.Name = "<<產品A>>";
//			a1.Price = new Decimal("<<單價>>");
//			a1.Currency = "<<幣別>>";
//			a1.Quantity = 0;
//			// <<數量>>
//			a1.URL = "<<產品說明位址>>";
//			oPayment.Send.Items.add(a1);
//			Item a2 = new Item();
//			a2.Name = "<<產品B>>";
//			a2.Price = new Decimal("<<單價>>");
//			a2.Currency = "<<幣別>>";
//			a2.Quantity = 0;
//			// <<數量>>
//			a2.URL = "<<產品說明位址>>";
//			oPayment.Send.Items.add(a2);
//			/* 產生訂單 */
//			enErrors.addAll(oPayment.CheckOut(response.getWriter()));
//			/* 產生產生訂單 Html Code 的方法 */
//			StringBuilder szHtml = new StringBuilder();
//			enErrors.addAll(oPayment.CheckOutString(szHtml));
//		} catch (Exception e) { // 例外錯誤處理。
//			enErrors.add(e.getMessage());
//		} finally { // 顯示錯誤訊息。
//			if (enErrors.size() > 0)
//				out.print(enErrors);
//		}
//	}
//
//}


