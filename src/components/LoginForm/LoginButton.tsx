import { useFormStatus } from "react-dom";
import React from "react";

export default function LoginButton() {
  const { pending } = useFormStatus();

  return (
    <button className="shadow-submit dark:shadow-submit-dark flex w-full
                    items-center justify-center rounded-sm bg-primary px-9 py-4 text-base font-medium text-white
                    duration-300 hover:bg-primary/90" aria-disabled={pending}>
      Войти
    </button>
  );
}