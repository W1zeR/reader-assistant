import { FormEvent } from "react";
import axios from "axios";

export default function RegisterForm() {
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const formData = new FormData(event.currentTarget);
    const password = formData.get("password");
    const confirmPassword = formData.get("confirmPassword");
    if (password === confirmPassword) {
      await axios.post(API_URL + "/auth/register", {
        email: formData.get("email"),
        login: formData.get("login"),
        password: password,
        roleId: 1
      });
    }
  }

  return (
    <>
      <form onSubmit={onSubmit}>
        <div className="mb-8">
          <label
            htmlFor="email"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            Адрес электронной почты{" "}
          </label>
          <input
            type="email"
            name="email"
            placeholder="Введите адрес электронной почты"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="login"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            Имя пользователя{" "}
          </label>
          <input
            type="text"
            name="login"
            placeholder="Введите имя пользователя"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="password"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            Пароль{" "}
          </label>
          <input
            type="password"
            name="password"
            placeholder="Введите пароль"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm
                      border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="confirmPassword"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            Подтвердите пароль{" "}
          </label>
          <input
            type="password"
            name="confirmPassword"
            placeholder="Повторно введите пароль"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-6">
          <button type="submit"
                  className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                      rounded-sm bg-primary px-9 py-4 text-base font-medium text-white duration-300
                      hover:bg-primary/90">
            Зарегистрироваться
          </button>
        </div>
      </form>
    </>
  );
};
