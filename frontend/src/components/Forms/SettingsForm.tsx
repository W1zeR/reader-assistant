"use client";

import { useSession } from "next-auth/react";
import { FormEvent, useEffect, useState } from "react";
import axios from "axios";

export default function SettingsForm() {
  const { data: session } = useSession();
  const [profile, setProfile] = useState(
    {
      id: 0,
      email: "",
      login: ""
    }
  );
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    axios.get(API_URL + `/profiles/me`, {
        headers: {
          Authorization: `Bearer ${session?.accessToken}`
        }
      }
    )
      .then(response => {
        setProfile(response.data);
      })
      .catch(error => {
        console.error(error);
      });
  });

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const formData = new FormData(event.currentTarget);
    await axios.put(API_URL + `/profiles/${profile.id}`, {
      email: formData.get("email"),
      login: formData.get("login")
    }, {
      headers: {
        Authorization: `Bearer ${session?.accessToken}`
      }
    });

    const oldPassword = formData.get("currentPassword");
    const newPassword = formData.get("newPassword");
    const confirmNewPassword = formData.get("confirmNewPassword");
    if (oldPassword != "" && newPassword != "" && oldPassword != newPassword && newPassword == confirmNewPassword) {
      await axios.put(API_URL + "/changePassword", {
        oldPassword: oldPassword,
        newPassword: newPassword
      }, {
        headers: {
          Authorization: `Bearer ${session?.accessToken}`
        }
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
            placeholder=""
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
            defaultValue={profile.email}
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
            placeholder=""
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
            defaultValue={profile.login}
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="currentPassword"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            Текущий пароль{" "}
          </label>
          <input
            type="password"
            name="currentPassword"
            placeholder="Введите текущий пароль"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="newPassword"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            Новый пароль{" "}
          </label>
          <input
            type="password"
            name="newPassword"
            placeholder="Введите новый пароль"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="confirmNewPassword"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            Подтвердите новый пароль{" "}
          </label>
          <input
            type="password"
            name="confirmNewPassword"
            placeholder="Повторно введите новый пароль"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-6">
          <button type="submit"
                  className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                    rounded-sm bg-primary px-9 py-4 text-base font-medium text-white duration-300 hover:bg-primary/90">
            Сохранить
          </button>
        </div>
        <div className="mb-6">
          <button
            className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                    rounded-sm bg-red-700 px-9 py-4 text-base font-medium text-white duration-300 hover:bg-red-700/90">
            Отмена
          </button>
        </div>
      </form>
    </>
  );
}
