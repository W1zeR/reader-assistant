"use client";

import { useFormState, useFormStatus } from "react-dom";
import { ExclamationCircleIcon } from "@heroicons/react/24/outline";
import React from "react";
import authenticate from "@/utils/authenticate";

export default function LoginForm() {
  const [errorMessage, dispatch] = useFormState(authenticate, undefined);
  const { pending } = useFormStatus();

  return (
    <form action={dispatch}>
      <div className="mb-8">
        <label
          htmlFor="email"
          className="mb-3 block text-sm text-dark dark:text-white"
        >
          Адрес электронной почты
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
          htmlFor="password"
          className="mb-3 block text-sm text-dark dark:text-white"
        >
          Пароль
        </label>
        <input
          type="password"
          name="password"
          placeholder="Введите пароль"
          className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
        />
      </div>

      <div className="mb-6">
        <button className="shadow-submit dark:shadow-submit-dark flex w-full
                    items-center justify-center rounded-sm bg-primary px-9 py-4 text-base font-medium text-white
                    duration-300 hover:bg-primary/90" aria-disabled={pending}>
          Войти
        </button>
        <div
          className="flex h-8 items-end space-x-1"
          aria-live="polite"
          aria-atomic="true"
        >
          {errorMessage && (
            <>
              <ExclamationCircleIcon className="h-5 w-5 text-red-500" />
              <p className="text-sm text-red-500">{errorMessage}</p>
            </>
          )}
        </div>
      </div>
    </form>
  );
}
