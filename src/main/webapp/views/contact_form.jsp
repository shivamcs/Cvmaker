<%@ include file="/resource-bundles/directives.jsp"%>
		<!-- Form container Start-->

		<div class="form-card" id="Contact">
			<h2>Contact</h2>
			
				
				
				<label>Email</label>
				<input type="text" name="email" placeholder="Email" value="${contact.email}">

				<label>Phone No</label>
				<input type="text" name="phone" placeholder="Phone Number" value="${contact.phone}">

				<label>Link/Website</label>
				<input type="text" name="link" placeholder="Link/Website" value="${contact.link}">
				
				
				
				<!-- Button container start-->
				<!-- <div class="button-container ">
					
					<input  class="btn btn-primary" type="submit" name="action" value="Save">
					<input  class="btn btn-primary" type="submit" name="action" value="Next">
				</div> -->
				<!-- Button container End-->
				

			
		</div>
		<!-- Form container End-->
