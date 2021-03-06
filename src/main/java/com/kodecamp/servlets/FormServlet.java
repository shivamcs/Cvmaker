package com.kodecamp.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kodecamp.Validation.impl.DateRangeValidation;
import com.kodecamp.Validation.impl.DateValidation;
import com.kodecamp.Validation.impl.EmailValidation;
import com.kodecamp.Validation.impl.EmptyValidator;
import com.kodecamp.Validation.impl.NullValidator;
import com.kodecamp.Validation.impl.PhoneNumberValidation;
import com.kodecamp.form.fragment.Experience;
import com.kodecamp.form.fragment.Skill;
import com.kodecamp.model.ExperienceModel;
import com.kodecamp.model.IntroductionModel;
import com.kodecamp.model.SkillModel;
import com.kodecamp.sessionmanager.PutFieldsValueInSession;
import com.kodecamp.validationapi.IValidationResult;
import com.kodecamp.validationapi.ValidationResult;

public class FormServlet extends HttpServlet {

	public FormServlet() {
		System.out.println(getClass().getName());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// doGet(req,resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// System.out.println("doGet() : "+getClass().getName());
		String action = req.getParameter("action") == null ? (String) req.getAttribute("action")
				: req.getParameter("action");
		String id = req.getParameter("id");
		String expButton = req.getParameter("buttonexp");
		String skillButton = req.getParameter("buttonskill");

		List messageList = new ArrayList();
		HttpSession session = req.getSession();

		// if action is equal to submit

		if ("Submit".equals(action)) {

			if (partOneValidation(req.getParameter("name"), req.getParameter("profession"), messageList)
					&& partTwoValidation(req.getParameter("email"), req.getParameter("phone"), req.getParameter("link"),
							messageList)
					&& partThreeValidation(req.getParameter("company"), req.getParameter("designation"),
							req.getParameter("fromDate"), req.getParameter("toDate"), req.getParameter("roll"),
							messageList)
					&& partFourValidation(req.getParameter("skills"), req.getParameter("profeciency"),
							req.getParameter("experience"), req.getParameter("months"), messageList)) {

			} else {
				// If Fail show error message from messageList
				req.setAttribute("messages", messageList);
			}

		}

		// if Action is equal to Add More
		if ("Add More".equals(expButton)) {

			if (partThreeValidation(req.getParameter("company"), req.getParameter("designation"),
					req.getParameter("fromDate"), req.getParameter("toDate"), req.getParameter("roll"), messageList)) {

				String expId = String.valueOf(id);
				Experience exp = new Experience(req.getParameter("company"), req.getParameter("designation"),
						req.getParameter("fromDate"), req.getParameter("toDate"), req.getParameter("roll"));

				ExperienceModel expModel = (ExperienceModel) session.getAttribute("expModel");
				if (expModel == null) {

					expModel = new ExperienceModel(req);
				}

				expModel.addExperience(exp);
				session.setAttribute("expModel", expModel);

			}

		}

		// if Add More of skill form is clicked

		if ("Add More".equals(skillButton)) {

			if (partFourValidation(req.getParameter("skills"), req.getParameter("profeciency"),
					req.getParameter("year"), req.getParameter("months"), messageList)) {

				Skill skill = new Skill(req.getParameter("skills"), req.getParameter("profeciency"),
						req.getParameter("year"), req.getParameter("months"));

				SkillModel skillModel = (SkillModel) session.getAttribute("skillModel");

				if (skillModel == null) {
					skillModel = new SkillModel(req);
				}

				skillModel.addSkill(skill);
				session.setAttribute("skillModel", skillModel);

			}
		}

		if ("Delete".equals(action)) {

			ExperienceModel expModel = (ExperienceModel) session.getAttribute("expModel");
			if (expModel == null) {
				expModel = new ExperienceModel(req);
			}
			expModel.deleteExperience(id);
		}

		if ("DeleteSkill".equals(action)) {

			SkillModel skillModel = (SkillModel) session.getAttribute("skillModel");
			if (skillModel == null) {
				skillModel = new SkillModel(req);
			}
			skillModel.deleteSkill(id);
		}
		
		PutFieldsValueInSession pfvs = new PutFieldsValueInSession(req, resp);
		pfvs.putValuesInSession();
		ServletContext context = getServletContext();
		context.getRequestDispatcher("/views/form.jsp").forward(req, resp);

	}

	/*
	 * Skills Validation
	 */
	private boolean partFourValidation(final String skillname, final String profeciency, final String year,
			final String months, List messageList) {

		// System.out.println("Part Four Validation called ");

		IValidationResult vr = null;
		boolean flag = true;

		vr = new EmptyValidator(new NullValidator()).validate(skillname);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}

		vr = new EmptyValidator(new NullValidator()).validate(profeciency);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}

		vr = new EmptyValidator(new NullValidator()).validate(year);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}

		vr = new EmptyValidator(new NullValidator()).validate(months);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}

		return flag;
	}

	/*
	 * Experience Validation
	 */
	private boolean partThreeValidation(String company, String designation, String fromDate, String toDate, String roll,
			List messageList) {

		// System.out.println("Part three Validation called...");

		IValidationResult vr = null;
		boolean flag = true;

		vr = new EmptyValidator(new NullValidator()).validate(company);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}

		vr = new EmptyValidator(new NullValidator()).validate(designation);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}

		flag = validateDate(fromDate, toDate, messageList);

		vr = new EmptyValidator(new NullValidator()).validate(roll);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}
		return flag;
	}

	/*
	 * Contact Validation
	 */
	private boolean partTwoValidation(String email, String phone, String link, List messageList) {

		// System.out.println("Part two Validation...");

		IValidationResult vr = null;
		boolean flag = true;
		vr = new EmailValidation(new EmptyValidator(new NullValidator())).validate(email);

		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}
		vr = new PhoneNumberValidation(new EmptyValidator(new NullValidator())).validate(phone);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}
		vr = new EmptyValidator(new NullValidator()).validate(link);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}
		return flag;
	}

	/*
	 * Introduction Validation
	 */
	private boolean partOneValidation(String name, String profession, List messageList) {

		// System.out.println("PartOne Valdation called ...");

		IValidationResult vr = null;
		boolean flag = true;
		vr = new EmptyValidator(new NullValidator()).validate(name);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}
		vr = new EmptyValidator(new NullValidator()).validate(profession);
		if (vr.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr.message());
		}

		return flag;
	}

	/*
	 * Date Validation
	 */
	private boolean validateDate(String fromDate, String toDate, List messageList) {

		// System.out.println("Value of toDate : "+toDate);
		IValidationResult vr1 = null;
		IValidationResult vr2 = null;

		boolean flag = true;
		vr1 = new DateValidation(new EmptyValidator(new NullValidator())).validate(fromDate);
		vr2 = new DateValidation(new EmptyValidator(new NullValidator())).validate(toDate);

		if (vr1.status().equals(ValidationResult.Status.FAIL) && vr2.status().equals(ValidationResult.Status.FAIL)) {
			flag = false;
			messageList.add(vr1.message());
			messageList.add(vr2.message());

		} else {
			IValidationResult vr = new DateRangeValidation(fromDate, toDate).validate();
			if (vr.status().equals(ValidationResult.Status.FAIL)) {
				flag = false;
				messageList.add(vr.message());
			}
		}
		return flag;
	}

}
